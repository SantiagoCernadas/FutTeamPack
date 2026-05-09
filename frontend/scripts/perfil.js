import { obtenerUsuario, obtenerEquiposPorUsuario } from './api.js'

const UrlObjeto = new URL(window.location.href);
const nickname = UrlObjeto.searchParams.get('nickname');


await imprimirUsuarioPerfilUsuario(nickname);





async function imprimirUsuarioPerfilUsuario(nickname) {
    try {
        const usuarioResponse = await obtenerUsuario(nickname);
        document.getElementById('nickname-usuario').textContent = usuarioResponse.nickname;
        document.getElementById('nickname-usuario-equipos').textContent = usuarioResponse.nickname;
        document.getElementById('imagen-club').src = usuarioResponse.imagen;
        document.getElementById('cant-sobres-abiertos').textContent = usuarioResponse.sobresAbiertos;

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
        return;
    }
    await imprimirEquiposUsuario(nickname);
}

async function imprimirEquiposUsuario(nickname) {
     const contenedorEquipos = document.querySelector('.contenedor-equipos');
    try {
        contenedorEquipos.innerHTML = "";
        const equipoResponse = await obtenerEquiposPorUsuario(nickname);
        const equiposLista = equipoResponse.content;
        document.getElementById('cant-equipos-desbloqueados').textContent = equipoResponse.totalElements;
        if (equiposLista.length > 0) {
            equiposLista.forEach((equipo) => {
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
            botonVerTodos.addEventListener('click', () => {
                window.location.href = "equipos_usuario.html?nickname=" + nickname + "&pagina=0&cantidad=5";
            })
            contenedorEquipos.appendChild(botonVerTodos);
        }
        else {
            const tituloSinEquipos = document.createElement('h2');
            tituloSinEquipos.textContent = "No tiene equipos."
            contenedorEquipos.appendChild(tituloSinEquipos);
        }
    }
    catch(err) {
        contenedorEquipos.innerHTML = "";
        const tituloError = document.createElement('h2');
        tituloError.textContent = "No fue posible obtener los equipos."
        contenedorEquipos.appendChild(tituloError);
    }

}