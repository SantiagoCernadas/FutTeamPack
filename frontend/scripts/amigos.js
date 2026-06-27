import { eliminarUsuario, enviarSolicitudAmistad, obtenerAmigosUsuario, obtenerSolicitudesUsuario, responderSolicitudAmistad } from './api.js';
import { finalizarCarga, iniciarCarga } from "./loader.js";


await imprimirAmigosUsuario(true);
await imprimirSolicitudesUsuario(true);

setInterval(() => {
    imprimirAmigosUsuario(false);
    imprimirSolicitudesUsuario(false);
}, 4000);




async function imprimirAmigosUsuario(carga = true) {
    const contenedor = document.querySelector('.amistades');

    if (carga) {
        iniciarCarga(contenedor);
    }

    try {
        const amigos = await obtenerAmigosUsuario();

        contenedor.innerHTML = "";

        if (amigos.length == 0) {
            const textoSinAmigos = document.createElement('h3');
            textoSinAmigos.textContent = "Actualmente no tienes amigos."
            contenedor.appendChild(textoSinAmigos);
        }
        else {
            amigos.forEach(amigo => {
                const contenedorAmigo = document.createElement('div');
                contenedorAmigo.classList.add('contenedor-amigo');

                const imagenAmigo = document.createElement('img');
                imagenAmigo.src = amigo.imagen;

                const nickAmigo = document.createElement('p');
                nickAmigo.textContent = amigo.nickname;

                contenedorAmigo.appendChild(imagenAmigo);
                contenedorAmigo.appendChild(nickAmigo);

                const botonPerfil = generarBotonPerfil(amigo.nickname);
                contenedorAmigo.appendChild(botonPerfil);

                const botonEliminar = generarBotonEliminar(amigo.nickname);
                contenedorAmigo.appendChild(botonEliminar);

                contenedor.appendChild(contenedorAmigo);
            });
        }
    } catch (err) {
        console.error(err.mensaje);
    }
    finally {
        if (carga) {
            finalizarCarga(contenedor);
        }
    }
}


function generarBotonPerfil(nickname) {
    const boton = document.createElement('button');
    boton.textContent = "Ver perfil"

    boton.addEventListener('click', () => {
        window.location.href = "perfil.html?nickname=" + nickname;
    })

    return boton;
}

function generarBotonEliminar(nickname) {
    const boton = document.createElement('button');
    boton.classList.add('boton-eliminar')
    boton.textContent = "Eliminar"

    boton.addEventListener('click', async () => {
        if (confirm("estas seguro de eliminar a el usuario: " + nickname + "?")) {
            try {
                boton.parentElement.classList.add('elemento-bloqueado');
                await eliminarUsuario(nickname);
                boton.parentElement.remove(boton.parentElement);
                if (document.querySelector('.amistades').childElementCount <= 0) {
                    const textoSinAmigos = document.createElement('h3');
                    textoSinAmigos.textContent = "Actualmente no tienes amigos."
                    document.querySelector('.amistades').appendChild(textoSinAmigos);
                }
            } catch (err) {
                alert("no fue eliminar la amistad: " + err.mensaje);
                boton.parentElement.classList.remove('elemento-bloqueado');
            }
        }
    })

    return boton;
}

function generarBotonAceptar(nickname) {
    const boton = document.createElement('button');
    boton.classList.add('boton-aceptar')
    boton.textContent = "Aceptar"

    boton.addEventListener('click', async () => {
        try {
            boton.parentElement.classList.add('elemento-bloqueado');
            await responderSolicitudAmistad(nickname, true);
            boton.parentElement.remove(boton.parentElement);
            if (document.querySelector('.solicitudes').childElementCount <= 0) {
                const textoSinSolicitudes = document.createElement('h3');
                textoSinSolicitudes.textContent = "Actualmente no tienes solicitudes pendientes."
                document.querySelector('.solicitudes').appendChild(textoSinSolicitudes);
            }
        } catch (err) {
            alert("no fue posible aceptar la solicitud: " + err.mensaje);
            boton.parentElement.classList.remove('elemento-bloqueado');
        }

    })

    return boton;
}

function generarBotonRechazar(nickname) {
    const boton = document.createElement('button');
    boton.classList.add('boton-rechazar')
    boton.textContent = "Rechazar"

    boton.addEventListener('click', async () => {
        try {
            boton.parentElement.classList.add('elemento-bloqueado');
            await responderSolicitudAmistad(nickname, false);
            boton.parentElement.remove(boton.parentElement);
            if (document.querySelector('.solicitudes').childElementCount <= 0) {
                const textoSinSolicitudes = document.createElement('h3');
                textoSinSolicitudes.textContent = "Actualmente no tienes amigos."
                document.querySelector('.solicitudes').appendChild(textoSinSolicitudes);
            }
        } catch (err) {
            alert("no fue posible rechazar la solicitud: " + err.mensaje);
            boton.parentElement.classList.remove('elemento-bloqueado');
        }

    })

    return boton;
}

async function imprimirSolicitudesUsuario(carga = false) {
    const contenedor = document.querySelector('.solicitudes');
    if (carga) {
        iniciarCarga(contenedor);
    }
    try {

        const solicitudes = await obtenerSolicitudesUsuario();
        contenedor.innerHTML = "";
        if (solicitudes.length == 0) {
            const textoSinSolicitudes = document.createElement('h3');
            textoSinSolicitudes.textContent = "Actualmente no tienes solicitudes pendientes."
            contenedor.appendChild(textoSinSolicitudes);
        }
        else {
            solicitudes.forEach(solicitud => {
                const contenedorSolicitud = document.createElement('div');
                contenedorSolicitud.classList.add('contenedor-amigo');

                const imagenSolicitud = document.createElement('img');
                imagenSolicitud.src = solicitud.imagen;

                const nickSolicitud = document.createElement('p');
                nickSolicitud.textContent = solicitud.nickname;

                contenedorSolicitud.appendChild(imagenSolicitud);
                contenedorSolicitud.appendChild(nickSolicitud);

                const botonAceptar = generarBotonAceptar(solicitud.nickname);
                contenedorSolicitud.appendChild(botonAceptar);

                const botonRechazar = generarBotonRechazar(solicitud.nickname);
                contenedorSolicitud.appendChild(botonRechazar);

                contenedor.appendChild(contenedorSolicitud);
            });
        }
    } catch (err) {
        console.error(err.mensaje);
    }
    finally {
        if (carga) finalizarCarga(contenedor);
    }
}

document.getElementById('boton-enviar-solicitud').addEventListener('click', async () => {
    const nickSolicitud = document.getElementById('input-nick-solicitud').value;
    if (nickSolicitud == "") {
        document.getElementById('mensaje-aviso-agregar').textContent = "Ingresar usuario";
        return;
    }
    await enviarSolicitudDeAmistad(nickSolicitud);
});

async function enviarSolicitudDeAmistad(nickSolicitud) {
    const contenedor = document.querySelector('.contenedor-agregar');

    try {
        contenedor.classList.add('elemento-bloqueado');
        await enviarSolicitudAmistad(nickSolicitud);
        document.getElementById('mensaje-aviso-agregar').style.color = "#18E767";
        document.getElementById('mensaje-aviso-agregar').textContent = 'Solicitud enviada!';
    } catch (err) {
        document.getElementById('mensaje-aviso-agregar').style.color = "#FF2E51";
        document.getElementById('mensaje-aviso-agregar').textContent = err.mensaje;
    }
    finally {
        contenedor.classList.remove('elemento-bloqueado');
    }
}

document.getElementById('input-nick-solicitud').addEventListener('input', limpiarMensajeNoti);

function limpiarMensajeNoti(event) {
    document.getElementById('mensaje-aviso-agregar').textContent = "";
}