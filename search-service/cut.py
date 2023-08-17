import json
import datetime

new_data = []
file_name = "movies-tenflix-0-1500.json"

with open(file_name, "r", encoding='utf-8') as f:
    my_list = json.load(f)

    for idx, obj in enumerate(my_list):
        if (obj["url"] == "" or obj["synopsis"] == ""):
            continue
        date1 = datetime.datetime.strptime(str(obj["releaseYear"]), "%Y-%m-%d").strftime("%Y")
        obj["image"] = "https://image.tmdb.org/t/p/original/" + str(obj["image"])
        obj["releaseYear"] = int(date1)
        new_data.append(obj)


file_name = "movies-tenflix-1500-3000.json"

with open(file_name, "r", encoding='utf-8') as f:
    my_list = json.load(f)

    for idx, obj in enumerate(my_list):
        if (obj["url"] == "" or obj["synopsis"] == ""):
            continue
        date1 = datetime.datetime.strptime(str(obj["releaseYear"]), "%Y-%m-%d").strftime("%Y")
        obj["image"] = "https://image.tmdb.org/t/p/original/" + str(obj["image"])
        obj["releaseYear"] = int(date1)
        new_data.append(obj)

file_name = "movies-tenflix-3000-4500.json"

with open(file_name, "r", encoding='utf-8') as f:
    my_list = json.load(f)

    for idx, obj in enumerate(my_list):
        if (obj["url"] == "" or obj["synopsis"] == ""):
            continue
        date1 = datetime.datetime.strptime(str(obj["releaseYear"]), "%Y-%m-%d").strftime("%Y")
        obj["image"] = "https://image.tmdb.org/t/p/original/" + str(obj["image"])
        obj["releaseYear"] = int(date1)
        new_data.append(obj)

file_name = "movies-tenflix-4500-6000.json"

with open(file_name, "r", encoding='utf-8') as f:
    my_list = json.load(f)

    for idx, obj in enumerate(my_list):
        if (obj["url"] == "" or obj["synopsis"] == ""):
            continue
        date1 = datetime.datetime.strptime(str(obj["releaseYear"]), "%Y-%m-%d").strftime("%Y")
        obj["image"] = "https://image.tmdb.org/t/p/original/" + str(obj["image"])
        obj["releaseYear"] = int(date1)
        new_data.append(obj)


file_name = "movies-tenflix-6000-end.json"

with open(file_name, "r", encoding='utf-8') as f:
    my_list = json.load(f)

    for idx, obj in enumerate(my_list):
        if (obj["url"] == "" or obj["synopsis"] == ""):
            continue
        date1 = datetime.datetime.strptime(str(obj["releaseYear"]), "%Y-%m-%d").strftime("%Y")
        print(obj["title"])
        obj["image"] = "https://image.tmdb.org/t/p/original/" + str(obj["image"])
        obj["releaseYear"] = int(date1)
        new_data.append(obj)





new_file_name = "movies-tenflix.json"

with open(new_file_name, 'w', encoding='utf-8') as f:
      json.dump(new_data, f, indent=4)