
from daily_woonse import get_woonse

res = get_woonse(
    sql_path=r"c:\Users\User\Desktop\coding\woonse\data\calendar.sql",
    birth_date="1999-04-17",        # 양력
    current_date="2025-10-28",
    current_time="02:10",
    day_start_hour=23
)
print(res)
