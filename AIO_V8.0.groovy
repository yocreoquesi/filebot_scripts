{/*FACTORIA DE RECICLAJE
script by @yocreoquesi*/

// Aux functions
def audiotitleaux =
    count = audio.size()
    audiotitles = []
    for (a in audio)
        {
            audiotitles.add(any{a.Title}{})
        }
    audiotitleaux = audiotitles


def audiolangaux =
    count = audio.size()
    audiolang = []
    for (a in audio)
        {
            audiolang.add(any{a.language}{})
        }
    audiolangaux = audiolang.unique()


def textlangaux =
    count = any{text.size()}{0}
    textlang = []
    for (a in any{text}{})
        {
            textlang.add(any{a.language}{})
        }
    textlangaux = textlang


// Regular functions
def pelicula = 
    plex.name.replaceAll(/[¡!¿?ºª]/, "").replaceAll(/[бвгджзийклмнптфцчшщьюя]/, "¿").isLatin()
    ? localize.Spanish.plex.name + ' ' 
    : localize.English.plex.name + ' '


def peli3d = 
    (
        fn =~ /3D/ 
        || 
        folder =~ /3D/ 
        || 
        any{s3d.size()}{0} > 0
    ) 
    ? '[3D]'  
    : ''


def calidad =
    (
        any{hd}{0} == 0
        ||
        any{vf}{0} == 0
        ||
        {bitrate}{0} == 0
    )
    ? '[CORRUPTED]'
    :

    (
        hd == 'UHD' 
        && 
        (bytes/1073741824).round(1) >= 30
        && 
        bitrate >= 26000000 
    ) 
    ? '[4K UHDremux]' 
    :

    (
        hd == 'HD' 
        && 
        vf == '1080p' 
        && 
        (bytes/1073741824).round(1) >= 15
        && 
        bitrate > 18000000 
    ) 
    ? '[BD-Remux]' 
    :

    (
        hd == 'SD'
    ) 
    ? '[SD]' 
    :

    (
        hd == 'UHD' 
    ) 
    ? '[4K MicroUHD]' 
    :

    (
        hd == 'HD' 
        && 
        vf == '720p'
    ) 
    ? '[720p]' 
    :

    (
        hd == 'HD' 
        && 
        vf == '1080p' 
        && 
        bitrate > 8000000 
    ) 
    ? '[BD-Rip]'

    : '[Micro-HD]'

def idioma =
        (   
            (
                audiolangaux.size() > 1 
                || 
                any{audioLanguages.size()}{0} > 1 
            )
            &&  
            (  
                !(audiotitleaux =~ /atin/)
                &&
                (
                    audiolangaux =~ /es|pañol/
                    ||
                    any{audioLanguages}{0} =~ /spa/
                    ||
                    audiotitleaux =~ /pañol|panol|ellano/
                )   
            )        
        ) ? '[Dual]' :

        (
            (
                (
                    audiolangaux.size() == 1
                    ||
                    any{audioLanguages.size}{0} == 1
                )
                &&
                !(audiotitleaux =~ /atino/)
                &&
                (
                    any{audioLanguages}{0} =~ /spa/
                    ||
                    audiolangaux =~ /es|pañol/
                )
            )
            || 
            (
                any{audio.size()}{0} == 1
                && 
                hd == 'SD'
                &&
                any{ext}{0} =~ /avi|mp4|mpg/
                && 
                any{text.size()}{0} == 0
            ) 
        ) ? '[ES]' :

        (
            audiolangaux.size() > 1 
            &&
            ( 
                audiotitleaux =~ /atino/
                || audiolangaux =~ /la/
            )
            
        ) ? '[Dual][LAT]' :

        (
            audiolangaux.size() == 1
            &&
            ( 
                audiotitleaux =~ /atino/
                || 
                audiolangaux =~ /la/
            )
        ) ? '[LAT]' :

        (
            textlangaux =~ /es/
            &&
            any{text.size()}{0} > 0    
        ) ? '[VOSE]' :

        (
            !(textlangaux =~ /es/)
            &&
            any{text.size()}{0} > 0
        ) ? '[VOS]' :

        ( 
            !(audiolangaux =~ /es/)
            && 
            any{text.size()}{0} == 0
        ) ? '[VO]' : '[NPI]'

def genero =
        (fn.contains('[Animación]')
        || any{genres}{0} =~ /Animación|Animation/
        ) ? '[Animación]' :
        (fn.contains('[Documental]')
        || any{genres}{0} =~ /Documental/
        ) ? '[Documental]' : 
        (audiotitleaux =~ /muda|Muda|mute|Mute|silent|Silent/ 
        ) ? '[Mudo]' : ''

def infantil =
        (fn.contains('[Infantil]')
        ) ? '[Infantil]' :
        (any{genres}{0} =~ /Animación|Animation/
        && (any{certification}{0} == 'G' || any{certification}{0} == 'PG')
        ) ? '[Infantil]' : ''

def metraje =
        (any{minutes}{0} == 0
        ) ? '[MetrajeNPI]' :
        (minutes <= 30 
        ) ? '[Cortometraje]' : ''

def codec =
        (any{vc}{0} =~ /HEVC|265|ATEME/
        ) ? '[x265]' : ''


def aiofactoria = pelicula + peli3d + calidad + idioma + genero + infantil + metraje + codec

return aiofactoria

/*
All In One script aka AIO script
propiedad de FACTORIA DE RECICLAJE
by @yocreoquesi


Versión AIO 8.0 


*/}