import eyed3

audiofile = eyed3.load("冠军相-黄旭&杨和苏.mp3")
audiofile.tag.title = "冠军相"
audiofile.tag.artist = "黄旭&杨和苏"

audiofile.tag.save()