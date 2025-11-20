
# -*- coding: utf-8 -*-
"""
daily_woonse.py (solar-only)

- Solar-only lookup for dates: cd_sy, cd_sm, cd_sd
- birth_date is interpreted as SOLAR only
- Robust SQL parser: supports backticks, column list, multi-row VALUES
- Ten Gods computation vs user day master
- CSV default path is embedded; keys: ('십성','분야') -> '콘텐츠'

CLI examples:
  python daily_woonse.py --sql "woonse/data/calendar.sql" --birth_date 1999-04-17 --current_date 2025-10-28 --current_time 02:10 --day_start_hour 23
  python daily_woonse.py --sql "woonse/data/calendar.sql" --dm 甲 --today_stem 癸
"""

import csv, os, re, argparse, json, datetime
from typing import Dict, Tuple, Optional, List

# ---------- Defaults ----------
DEFAULT_CSV_PATH = "data/일간_.csv"  # fixed path

# ---------- Ten Gods ----------
STEM_INFO = {
    '甲': ('木', 0), '乙': ('木', 1),
    '丙': ('火', 0), '丁': ('火', 1),
    '戊': ('土', 0), '己': ('土', 1),
    '庚': ('金', 0), '辛': ('金', 1),
    '壬': ('水', 0), '癸': ('水', 1),
}
ORDER = ['木','火','土','金','水']
IDX = {e:i for i,e in enumerate(ORDER)}

def _rel(a_elem: str, b_elem: str) -> str:
    ai, bi = IDX[a_elem], IDX[b_elem]
    if ai == bi: return 'same'
    if (ai + 1) % 5 == bi: return 'I-generate'   # 식상
    if (ai + 4) % 5 == bi: return 'generate-me'  # 인성
    if (ai + 2) % 5 == bi: return 'I-control'    # 재성
    if (ai + 3) % 5 == bi: return 'control-me'   # 관성
    return 'unknown'

def ten_god(day_master: str, other_stem: str) -> str:
    a_elem, a_yin = STEM_INFO[day_master]
    b_elem, b_yin = STEM_INFO[other_stem]
    rel = _rel(a_elem, b_elem)
    same = (a_yin == b_yin)
    if rel == 'same':        return '비견' if same else '겁재'
    if rel == 'I-generate':  return '식신' if same else '상관'
    if rel == 'I-control':   return '정재' if not same else '편재'
    if rel == 'control-me':  return '정관' if not same else '편관'
    if rel == 'generate-me': return '정인' if not same else '편인'
    return '미정'

def _stem_of_gz(gz: str) -> str:
    gz = (gz or '').strip()
    return gz[0] if gz else ''

# ---------- SQL robust parser (calenda_data) ----------
# default 0-based indices for INSERTs without column list
_DEF_IDX = {'cd_sy': 2, 'cd_sm': 3, 'cd_sd': 4, 'cd_hdganjee': 12}

def _dequote(x: Optional[str]) -> Optional[str]:
    if x is None: return None
    s = x.strip()
    if s.upper() == 'NULL': return None
    if len(s) >= 2 and ((s[0] == "'" and s[-1] == "'") or (s[0] == '"' and s[-1] == '"')):
        return s[1:-1]
    return s

def _split_values(s: str) -> List[str]:
    out, token, inq, qch = [], '', False, ''
    for ch in s:
        if inq:
            token += ch
            if ch == qch:
                inq = False
            continue
        if ch in ("'", '"'):
            inq, qch = True, ch
            token += ch; continue
        if ch == ',':
            out.append(token.strip()); token = ''; continue
        token += ch
    if token: out.append(token.strip())
    return out

def parse_insert_blocks(sql_text: str):
    """
    Returns list of (columns_or_None, rows)
      - columns_or_None: list[str] or None (if no column list in INSERT)
      - rows: list[list[str|None]]
    Supports:
      INSERT INTO calenda_data VALUES (...),(...);
      INSERT INTO `calenda_data` (cd_no, cd_sgi, ...) VALUES (...),(...);
    """
    insert_re = re.compile(
        r"INSERT\s+INTO\s+`?calenda_data`?\s*(\((?:[^)]*)\))?\s*VALUES\s*(.+?);",
        re.IGNORECASE | re.DOTALL
    )
    blocks = []
    for m in insert_re.finditer(sql_text):
        cols_part = m.group(1)
        values_part = m.group(2)
        columns = None
        if cols_part:
            inside = cols_part.strip()[1:-1]
            cols, token, inq, qch = [], '', False, ''
            for ch in inside:
                if inq:
                    token += ch
                    if ch == qch:
                        inq = False
                    continue
                if ch in ("'", '"'):
                    inq, qch = True, ch
                    token += ch; continue
                if ch == ',':
                    cols.append(token.strip().strip('`')); token = ''; continue
                token += ch
            if token: cols.append(token.strip().strip('`'))
            columns = [c.strip().strip('`') for c in cols]

        # split "(...),(...)" to tuples at top-level
        tuple_texts, lvl, cur = [], 0, []
        for ch in values_part:
            if ch == '(':
                if lvl == 0: cur = []
                lvl += 1
            if lvl > 0: cur.append(ch)
            if ch == ')':
                lvl -= 1
                if lvl == 0:
                    tuple_texts.append(''.join(cur)[1:-1])
        rows = [[_dequote(x) for x in _split_values(tup)] for tup in tuple_texts]
        blocks.append((columns, rows))
    return blocks

def _decode_sql_text(sql_path: str) -> str:
    raw = open(sql_path, 'rb').read()
    for enc in ('utf-8','utf-8-sig','cp949','euc-kr'):
        try:
            return raw.decode(enc)
        except Exception:
            continue
    return raw.decode('utf-8', errors='replace')

def _norm_num_str(v: Optional[str]) -> str:
    s = (v or '').strip().strip("'").strip('"')
    try:
        return str(int(s))
    except Exception:
        return s

def find_day_gz_from_sql(sql_path: str, y: int, m: int, d: int) -> str:
    """
    Solar-only: match by cd_sy, cd_sm, cd_sd; return cd_hdganjee.
    """
    text = _decode_sql_text(sql_path)
    y_s, m_s, d_s = str(y), str(m), str(d)

    for columns, rows in parse_insert_blocks(text):
        if columns:
            try:
                idx_sy = columns.index('cd_sy')
                idx_sm = columns.index('cd_sm')
                idx_sd = columns.index('cd_sd')
                idx_gz = columns.index('cd_hdganjee')
            except ValueError:
                continue
        else:
            idx_sy = _DEF_IDX['cd_sy']
            idx_sm = _DEF_IDX['cd_sm']
            idx_sd = _DEF_IDX['cd_sd']
            idx_gz = _DEF_IDX['cd_hdganjee']

        for row in rows:
            if len(row) <= max(idx_sy, idx_sm, idx_sd, idx_gz):
                continue
            if (_norm_num_str(row[idx_sy]) == y_s and
                _norm_num_str(row[idx_sm]) == m_s and
                _norm_num_str(row[idx_sd]) == d_s):
                return (row[idx_gz] or '').strip()

    raise LookupError("date not found in SQL (solar only)")

# ---------- Day boundary (for today only) ----------
def _apply_day_boundary(date_str: str, time_str: Optional[str], day_start_hour: int) -> str:
    y, m, d = [int(x) for x in date_str.split('-')]
    if not time_str: return date_str
    h, mi = [int(x) for x in time_str.split(':')[:2]]
    dt = datetime.datetime(y, m, d)
    if h < day_start_hour:
        dt = dt - datetime.timedelta(days=1)
    return dt.strftime('%Y-%m-%d')

# ---------- CSV lookup ----------
def _load_csv_index(csv_path: str, key_cols=('십성','분야'), text_col='콘텐츠') -> Dict[Tuple[str,str], Dict[str,str]]:
    idx: Dict[Tuple[str,str], Dict[str,str]] = {}
    last_err=None
    for enc in ('utf-8','utf-8-sig','cp949','euc-kr'):
        try:
            with open(csv_path, 'r', encoding=enc, newline='') as f:
                rdr = csv.DictReader(f)
                for row in rdr:
                    key = tuple((row[k] or '').strip() for k in key_cols)
                    idx[key] = {k:(v.strip() if isinstance(v,str) else v) for k,v in row.items()}
            return idx
        except Exception as e:
            last_err = e
            continue
    raise last_err

# ---------- Public API ----------
def get_woonse(sql_path: str,
               user_day_master: Optional[str] = None,
               # today resolution
               date_ymd: Optional[str] = None,
               today_day_gz: Optional[str] = None,
               today_stem: Optional[str] = None,
               current_date: Optional[str] = None,
               current_time: Optional[str] = None,
               day_start_hour: int = 0,
               # birth SOLAR only
               birth_date: Optional[str] = None,
               # CSV override (optional)
               csv_path: Optional[str] = None) -> dict:
    """
    Returns {'총운','관계팁','십성','today_stem','today_gz','user_day_master'}.
    - csv_path defaults to DEFAULT_CSV_PATH
    - birth_date is SOLAR only (no birth time)
    - Today stem precedence: today_stem/today_day_gz > (current_date[/time] + sql) > (date_ymd + sql)
    """
    csv_path = csv_path or DEFAULT_CSV_PATH

    # user day master
    if not user_day_master:
        if not birth_date:
            raise ValueError("Provide user_day_master or birth_date (solar)")
        yb, mb, db = [int(x) for x in birth_date.split('-')]
        gz_birth = find_day_gz_from_sql(sql_path, yb, mb, db)  # solar-only
        user_day_master = _stem_of_gz(gz_birth)

    if user_day_master not in STEM_INFO:
        raise ValueError("user_day_master must be one of 甲乙丙丁戊己庚辛壬癸")

    # today stem
    gz_today, stem_today = None, None
    if today_stem or today_day_gz:
        stem_today = (today_stem or _stem_of_gz(today_day_gz)).strip()
        gz_today = today_day_gz
    else:
        if current_date:
            base = _apply_day_boundary(current_date, current_time, day_start_hour)
            yt, mt, dt = [int(x) for x in base.split('-')]
            gz_today = find_day_gz_from_sql(sql_path, yt, mt, dt)  # solar-only
            stem_today = _stem_of_gz(gz_today)
        elif date_ymd:
            yt, mt, dt = [int(x) for x in date_ymd.split('-')]
            gz_today = find_day_gz_from_sql(sql_path, yt, mt, dt)  # solar-only
            stem_today = _stem_of_gz(gz_today)
        else:
            raise ValueError("Provide today_stem/today_day_gz or current_date[/time]+sql or date_ymd+sql")

    tg = ten_god(user_day_master, stem_today)
    idx = _load_csv_index(csv_path, key_cols=('십성','분야'), text_col='콘텐츠')
    res = {
        '총운': idx.get((tg,'총운'),{}).get('콘텐츠','(자료 없음)'),
        '관계팁': idx.get((tg,'관계팁'),{}).get('콘텐츠','(자료 없음)'),
        '십성': tg,
        'today_stem': stem_today,
        'user_day_master': user_day_master
    }
    if gz_today: res['today_gz'] = gz_today
    return res

# ---------- CLI ----------
def _cli():
    ap = argparse.ArgumentParser(description="Daily fortune (fixed CSV; solar-only)")
    ap.add_argument('--sql', required=True, help='SQL dump path (calenda_data INSERTs)')
    ap.add_argument('--dm', default=None, help='User day master (甲~癸). If absent, birth_date is required.')
    ap.add_argument('--birth_date', default=None, help='YYYY-MM-DD (SOLAR only)')
    ap.add_argument('--date', default=None, help='YYYY-MM-DD (today by date, SOLAR)')
    ap.add_argument('--today_gz', default=None, help='e.g., 癸酉')
    ap.add_argument('--today_stem', default=None, help='e.g., 癸')
    ap.add_argument('--current_date', default=None, help='YYYY-MM-DD (SOLAR)')
    ap.add_argument('--current_time', default=None, help='HH:MM')
    ap.add_argument('--day_start_hour', type=int, default=0, help='0 or 23 commonly')
    ap.add_argument('--csv', default=None, help='Override CSV path (optional)')
    args = ap.parse_args()

    res = get_woonse(
        sql_path=args.sql,
        user_day_master=args.dm,
        date_ymd=args.date,
        today_day_gz=args.today_gz,
        today_stem=args.today_stem,
        current_date=args.current_date,
        current_time=args.current_time,
        day_start_hour=args.day_start_hour,
        birth_date=args.birth_date,
        csv_path=args.csv or None
    )
    print(json.dumps(res, ensure_ascii=False))

if __name__ == '__main__':
    _cli()
