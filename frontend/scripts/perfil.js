import { obtenerUsuario, obtenerEquiposPorUsuario } from './api.js'

const UrlObjeto = new URL(window.location.href);

try {
    const usuarioResponse = await obtenerUsuario(UrlObjeto.searchParams.get('nickname'));
    document.getElementById('nickname-usuario').textContent = usuarioResponse.nickname;
    document.getElementById('nickname-usuario-equipos').textContent = usuarioResponse.nickname;
    document.getElementById('imagen-club').src = usuarioResponse.imagen;
    document.getElementById('cant-equipos-desbloqueados').textContent = 0;
    document.getElementById('cant-sobres-abiertos').textContent = usuarioResponse.sobresAbiertos;
    const contenedorEquipos = document.querySelector('.contenedor-equipos');
    contenedorEquipos.innerHTML = "";
    const equipoResponse = await obtenerEquiposPorUsuario(UrlObjeto.searchParams.get('nickname'));
    if (equipoResponse.length > 0) {
        equipoResponse.forEach((equipo) => {
            const contenedorEquipo = document.createElement('div');
            contenedorEquipo.classList.add('contenedor-equipo');
            const imagenEquipo = document.createElement('img');
            imagenEquipo.src = equipo.imagen;
            contenedorEquipo.appendChild(imagenEquipo);
            contenedorEquipos.appendChild(contenedorEquipo);
        });
        const botonVerTodos = document.createElement('button');
        botonVerTodos.classList.add('boton-ver-todos');
        botonVerTodos.textContent = "Ver todos los equipos";
        contenedorEquipos.appendChild(botonVerTodos);
    }
    else {
        const tituloSinEquipos = document.createElement('h2');
        tituloSinEquipos.textContent = "No tiene equipos."
        contenedorEquipos.appendChild(tituloSinEquipos);
    }


} catch (err) {
    const contenedorPagina = document.querySelector('.contenedor-perfil-datos');
    contenedorPagina.innerHTML = "";
    const contenedorError = document.createElement('div');
    const numError = document.createElement('h2');
    numError.textContent = "Error: " + err.estado;
    contenedorError.appendChild(numError);
    const mensajeError = document.createElement('p');
    mensajeError.textContent = err.mensaje;
    contenedorError.appendChild(mensajeError);
    contenedorPagina.appendChild(contenedorError);
}
