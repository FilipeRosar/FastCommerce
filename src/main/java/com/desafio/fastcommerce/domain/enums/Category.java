package com.desafio.fastcommerce.domain.enums;

import lombok.Getter;

@Getter
public enum Category {

    NOTEBOOKS("Notebooks"),
    COMPUTADORES("Computadores"),
    MONITORES("Monitores"),

    CELULARES("Celulares"),
    TABLETS("Tablets"),
    SMARTWATCHES("Smartwatches"),

    FONES_DE_OUVIDO("Fones de Ouvido"),
    CAIXAS_DE_SOM("Caixas de Som"),
    AUDIO_PROFISSIONAL("Áudio Profissional"),

    GAMES("Games"),
    CONSOLES("Consoles"),
    CADEIRAS_GAMER("Cadeiras Gamer"),

    TECLADOS("Teclados"),
    MOUSES("Mouses"),
    WEBCAMS("Webcams"),
    IMPRESSORAS("Impressoras"),

    PROCESSADORES("Processadores"),
    PLACAS_DE_VIDEO("Placas de Vídeo"),
    PLACAS_MAE("Placas-mãe"),
    MEMORIAS("Memórias RAM"),
    SSDS("SSDs"),
    HDS("HDs"),
    FONTES("Fontes"),
    GABINETES("Gabinetes"),

    REDES("Redes"),
    ROTEADORES("Roteadores"),

    SMART_HOME("Smart Home"),
    SEGURANCA("Segurança Eletrônica"),

    ACESSORIOS("Acessórios");

    private final String description;

    Category(String description) {
        this.description = description;
    }
}