{/*script by @yocreoquesi*/


// Aux functions

def decadaaux = ((y-5)/10).round(0) + '0s'

def seriefullaux = model.episodes.flatten().containsAll(episodelist.findAll{ !it.special })

def temporadaaux = (any{s}{0} < 9 ) ?  '0' + any{s}{0} : any{s}{0} 

// Regular functions
def teampath =
        ruta = folder
        while (ruta.parentFile != folder.root)
                {ruta = ruta.parentFile}
        teampath = ruta

def tipo =
        (type =~ /Episode/) ?
        '/Series/' :
        (type =~ /Movie/) ?
        '/Películas/' :
        '/' + type + '/'

def genero = 
        (   
            (any{genres}{0} =~ /Documentary/)
        ) ? '/Documentales/' :
        (   
            (any{genres}{0} =~ /Children/)
            &&
            (any{anime}{0})
        ) ? '/Anime/Infantiles/' :
        (   
            (any{anime}{0}) || (any{genres}{0} =~ /Anime/)
        ) ? '/Anime/No Infantiles/'  :
        (   
            (any{genres}{0} =~ /Children/)
        ) ? '/Animación/Infantiles/' :
        (   
            (any{genres}{0} =~ /Animation/) 
        ) ? '/Animación/No Infantiles/' :
        (
            (any{genres}{0} =~ /Mini-Series/)
        ) ? '/Mini Series/' :
        (   
            (any{genres}{0} =~ /Soap/) 
        ) ? '/Telenovelas/' :
        (
            (any{genres}{0} =~ /Show|Reality|News|Garden|Food/) 
        ) ? '/Tv Shows/' : '/Series/'


def decada = '/' + decadaaux + '/'

def estado = 
        (info.Status =~ /Ended/) 
        ? '/Finalizadas/' 
        :
        (info.Status =~ /Continuing/)
        ? '/En Emisión/' 
        : 'Otros-Testear'

def completa =
        (
                model.episodes.flatten().containsAll(episodelist.findAll{ !it.special })
                &&
                info.Status =~ /Ended/
        ) ? '/Completas/' 
        : 
        (
                info.Status =~ /Ended/
        ) ? '/Incompletas/'
        : ''

def nombreserie = 
        ( plex[1].replaceAll(/[¡!¿?ºª]/, "").replaceAll(/[бвгджзийклмнптфцчшщьюя]/, "¿").isLatin())
        ? '/' + localize.Spanish.plex[1] + ' (' + y + ')/'
        : '/' + localize.English.plex[1] + ' (' + y + ')/'
        
def temporada =
        (
            (
                any{s}{0} == 0
                &&
                !(model.episodes.flatten().containsAll(episodelist.findAll{it.special}))
            )
            ||
            !(
                model.episodes.flatten().containsAll(episodelist.findAll{it.season == any{s}{0}})  
            )
        ) 
        ? '/Temporada ' + temporadaaux + ' (INCOMPLETA)/'
        :
        (
            model.episodes.flatten().containsAll(episodelist.findAll{it.season == any{s}{0}}) 
        ) 
        ? '/Temporada ' + temporadaaux + '/' 
        : '/Temporada' + temporadaaux + ' REVISAR SCRIPT/'


def nombrecap =
    '/' + (plex[3])

def neoseries = teampath + tipo + genero + decada + estado + completa + nombreserie + temporada + nombrecap

return neoseries

/* NEOS V0.99
script by @yocreoquesi*/

}