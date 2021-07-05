package com.soten.musicplayer.service

data class MusicDto(
    val musics: List<MusicEntity> // 서버에서 내려오는 객체 뷰에서 사용하는 객체는 MisicModel로 사용할 것임
)