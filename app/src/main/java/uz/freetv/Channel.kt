package uz.freetv

data class Channel(
    val id: String,
    val name: String,
    val url: String,
    val category: String = "Umumiy"
)

object Channels {
    val categories = listOf(
        "Barchasi",
        "Umumiy",
        "Sport",
        "Musiqa",
        "Hududiy",
        "Radio"
    )

    val list = listOf(
        Channel("ozbekiston", "O'zbekiston", "https://stream8.cinerama.uz/1001/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("yoshlar", "Yoshlar", "https://stream8.cinerama.uz/1002/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("toshkent", "Toshkent", "https://stream8.cinerama.uz/1003/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("madaniyat", "Madaniyat va ma'rifat", "https://stream8.cinerama.uz/1005/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("dunyo", "Dunyo bo'ylab", "https://stream8.cinerama.uz/1006/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("bolajon", "Bolajon", "https://stream8.cinerama.uz/1007/playlist.m3u8", "Umumiy"),
        Channel("oz24", "O'zbekiston 24", "https://stream8.cinerama.uz/1011/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("mahalla", "Mahalla", "https://stream8.cinerama.uz/1013/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("milliy", "Milliy", "https://stream8.cinerama.uz/1014/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("uzreport", "UzReport TV", "https://stream8.cinerama.uz/1015/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("zor", "Zo'r TV", "https://stream8.cinerama.uz/1016/tracks-v1a1/mono.m3u8", "Umumiy"),
        Channel("sevimli", "Sevimli TV", "https://stream8.cinerama.uz/1017/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("biztv", "BIZ TV", "https://fl.biztv.media/biz_tv_720_uni8jhub4h8fub4idejswh8dh3j94finbu4nidj39inwsj92in3d/index.m3u8", "Umumiy"),
        Channel("my5", "MY5", "https://st.my5.media/hls/hd/index.m3u8", "Umumiy"),
        Channel("renessans", "Renessans TV", "https://stream8.cinerama.uz/1221/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("nurafshon", "Nurafshon TV", "https://stream8.cinerama.uz/1220/tracks-v1a1/mono.m3u8", "Umumiy"),
        Channel("aqlvoy", "Aqlvoy", "https://stream8.cinerama.uz/1205/tracks-v1a1/mono.m3u8", "Umumiy"),
        Channel("dasturxon", "Dasturxon TV", "https://stream8.cinerama.uz/1206/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("tarix", "O'zbekiston Tarixi", "https://stream8.cinerama.uz/1209/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("kinoteatr", "Kinoteatr", "https://stream8.cinerama.uz/1009/tracks-v1a1/playlist.m3u8", "Umumiy"),
        Channel("bizcinema", "BIZ Cinema", "https://fl.biztv.media/cinema_720_EMfSyXgoRdiIHgldXTZICucKTIeCKO/index.m3u8", "Umumiy"),
        Channel("sport", "Sport", "https://stream8.cinerama.uz/1004/tracks-v1a1/mono.m3u8", "Sport"),
        Channel("futbol", "Futbol TV", "http://stream3.cinerama.uz/1010/tracks-v1a1/mono.m3u8", "Sport"),
        Channel("navo", "Navo", "https://stream8.cinerama.uz/1008/tracks-v1a1/playlist.m3u8", "Musiqa"),
        Channel("ftv", "FTV", "https://stream8.cinerama.uz/1018/playlist.m3u8", "Musiqa"),
        Channel("bizmusic", "BIZ Music", "https://fl.biztv.media/music_720_QAKpGmVUjaPApCNjpsgBxrdqNihAkl/index.m3u8", "Musiqa"),
        Channel("andijon", "Andijon MTRK", "https://stream8.cinerama.uz/1457/tracks-v1a1/mono.m3u8", "Hududiy"),
        Channel("fargona", "Farg'ona MTRK", "https://stream8.cinerama.uz/1458/tracks-v1a1/mono.m3u8", "Hududiy"),
        Channel("buxoro", "Buxoro MTRK", "https://stream8.cinerama.uz/1459/tracks-v1a1/mono.m3u8", "Hududiy"),
        Channel("navoiy", "Navoiy MTRK", "https://stream8.cinerama.uz/1460/tracks-v1a1/mono.m3u8", "Hududiy"),
        Channel("qaraqalpaq", "Qaraqalpaqstan", "https://stream8.cinerama.uz/1467/playlist.m3u8", "Hududiy"),
        Channel("radio_ori", "O'zbekiston Radio", "https://stream8.cinerama.uz/1001/tracks-v1a1/playlist.m3u8", "Radio"),
        Channel("radio_yoshlar", "Yoshlar Radio", "https://stream8.cinerama.uz/1002/tracks-v1a1/playlist.m3u8", "Radio"),
        Channel("radio_mahalla", "Mahalla Radio", "https://stream8.cinerama.uz/1013/tracks-v1a1/playlist.m3u8", "Radio"),
        Channel("radio_navo", "Navo Radio", "https://stream8.cinerama.uz/1008/tracks-v1a1/playlist.m3u8", "Radio"),
        Channel("radio_ftv", "FTV Radio", "https://stream8.cinerama.uz/1018/playlist.m3u8", "Radio"),
    )
}
