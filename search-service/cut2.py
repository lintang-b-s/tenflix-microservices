
import json
import datetime
new_data = []
file_name = "movies-tenflix.json"

with open(file_name, "r", encoding='utf-8') as f:
    my_list = json.load(f)

    for idx, obj in enumerate(my_list):
      #  {"index":{}}
        # index = { "create": {"_index": "movieswiki", "_id": str(idx)}}
        # new_data.append(index)
        # obj["id"] = str(idx)
        new_data.append(obj)


with open("bulk-movies.json", 'w', encoding='utf-8') as f:
      json.dump(new_data, f, indent=4)