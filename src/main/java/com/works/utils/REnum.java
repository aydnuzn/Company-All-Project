package com.works.utils;

public enum REnum {
    STATUS,
    MESSAGE, // Api Mesajlarının açıklaması
    COUNT,
    RESULT,
    ERROR,   //Validation hataları
    COUNTOFPAGE, //Pageable yapıldıktan sonra getirilen sayfadaki sonuç sayısı
    REF, // Sifre unutma istegi gonderildiginde mai ile yollanan referans numarasi
    MAILSEND,
    DRAW, // pagenable içindeki draw
    RECORDS_FILTERED // SAYFADAKİ VERİ SAYISI
}
