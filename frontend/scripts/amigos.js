import { enviarSolicitudAmistad, obtenerAmigosUsuario,obtenerSolicitudesUsuario } from './api.js';
import { finalizarCarga, iniciarCarga } from "./loader.js";


await imprimirAmigosUsuario();
await imprimirSolicitudesUsuario();

async function imprimirAmigosUsuario() {
    const contenedor = document.querySelector('.amistades');
    contenedor.innerHTML = "";
    try {
        iniciarCarga(contenedor);
        const amigos = await obtenerAmigosUsuario();
        if(amigos.length == 0){
            const textoSinAmigos = document.createElement('h3');
            textoSinAmigos.textContent = "Actualmente no tienes amigos."
            contenedor.appendChild(textoSinAmigos);
        }
        else{
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
        alert(err.mensaje);
    }
    finally {
        finalizarCarga(contenedor);
    }
}



function generarBotonPerfil(nickname){
    const boton = document.createElement('button');
    boton.textContent = "Ver perfil"

    boton.addEventListener('click', () => {
        window.location.href = "perfil.html?nickname=" + nickname;
    })

    return boton;
}

function generarBotonEliminar(nickname){
    const boton = document.createElement('button');
    boton.classList.add('boton-eliminar')
    boton.textContent = "Eliminar"

    boton.addEventListener('click', () => {
        alert("Eliminar");
    })

    return boton;
}

function generarBotonAceptar(nickname){
    const boton = document.createElement('button');
    boton.classList.add('boton-aceptar')
    boton.textContent = "Aceptar"

    boton.addEventListener('click', () => {
        alert("Aceptar");
    })

    return boton;
}

function generarBotonRechazar(nickname){
    const boton = document.createElement('button');
    boton.classList.add('boton-rechazar')
    boton.textContent = "Rechazar"

    boton.addEventListener('click', () => {
        alert("Rechazar");
    })

    return boton;
}

async function imprimirSolicitudesUsuario() {
    const contenedor = document.querySelector('.solicitudes');
    contenedor.innerHTML = "";
    try {
        iniciarCarga(contenedor);
        const solicitudes = await obtenerSolicitudesUsuario();
        if(solicitudes.length == 0){
            const textoSinSolicitudes = document.createElement('h3');
            textoSinSolicitudes.textContent = "Actualmente no tienes solicitudes pendientes."
            contenedor.appendChild(textoSinSolicitudes);
        }
        else{
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
        alert(err.mensaje);
    }
    finally {
        finalizarCarga(contenedor);
    }
}

document.getElementById('boton-enviar-solicitud').addEventListener('click', async () => {
    const nickSolicitud = document.getElementById('input-nick-solicitud').value;
    if(nickSolicitud == ""){
        document.getElementById('mensaje-aviso-agregar').textContent = "Ingresar usuario";
        return;
    }
    await enviarSolicitudDeAmistad(nickSolicitud);
});

async function enviarSolicitudDeAmistad(nickSolicitud){
    const contenedor = document.querySelector('.contenedor-agregar');
    
    try{
        iniciarCarga(contenedor);
        await enviarSolicitudAmistad(nickSolicitud);
    } catch (err){
        document.getElementById('mensaje-aviso-agregar').textContent = err.mensaje;
    }
    finally{
        finalizarCarga(contenedor);
    }
}

document.getElementById('input-nick-solicitud').addEventListener('input',limpiarMensajeNoti);

function limpiarMensajeNoti(event){
    document.getElementById('mensaje-aviso-agregar').textContent = "";
}