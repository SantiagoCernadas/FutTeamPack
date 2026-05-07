import {obtenerUsuario} from './api.js'

const UrlObjeto = new URL(window.location.href);

try{
    const usuarioResponse = await obtenerUsuario(UrlObjeto.searchParams.get('nickname'));
    document.getElementById('nickname-usuario').textContent = usuarioResponse.nickname;
    document.getElementById('nickname-usuario-equipos').textContent = usuarioResponse.nickname;
    document.getElementById('imagen-club').src = usuarioResponse.imagen;
    document.getElementById('cant-equipos-desbloqueados').textContent = 0;
    document.getElementById('cant-sobres-abiertos').textContent = usuarioResponse.sobresAbiertos;
    //construir el usuario.
}catch(err){
   
}
