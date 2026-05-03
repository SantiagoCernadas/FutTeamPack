import { getSobresDisponibles,abrirSobre } from "./api.js";


const contenedor = document.querySelector('.contenedor-sobres');

async function sobresDisponibles() {
    const sobres = await getSobresDisponibles();
    return sobres;
}

async function imprimirSobres() {
    const listaSobres = await sobresDisponibles();

    contenedor.innerHTML = "";

    listaSobres.forEach((sobre) => {

        const sobreHTML = document.createElement('div');
        sobreHTML.classList.add('contenedor-sobre');

        // Título
        const tituloSobre = document.createElement('h3');
        const tipoSobre = sobre.tipoSobre.toLowerCase();
        tituloSobre.textContent = "Sobre " + tipoSobre;
        sobreHTML.appendChild(tituloSobre);

        // Imagen
        const imagen = document.createElement('img');
        imagen.classList.add('imagen');
        imagen.src = sobre.imagen;
        sobreHTML.appendChild(imagen);

        // Contenedor de acción (Donde irá el cronómetro o el botón)
        const areaAccion = document.createElement('div');
        areaAccion.classList.add('area-accion');
        
        // Lógica de disponibilidad inicial
        // Asumiendo que 'sobre.tiempoRestante' viene en segundos desde el backend
        let tiempoRestante = sobre.segundosRestantes; 

        if (tiempoRestante <= 0) {
            mostrarBotonAbrir(areaAccion,tipoSobre);
        } else {
            iniciarCronometro(areaAccion, tiempoRestante,tipoSobre);
        }

        sobreHTML.appendChild(areaAccion);
        contenedor.appendChild(sobreHTML);
    });
}

function iniciarCronometro(contenedor, segundos,tipoSobre) {
    const pInfo = document.createElement('p');
    pInfo.textContent = "Disponible en:";
    
    const pTiempo = document.createElement('p');
    pTiempo.classList.add('cronometro');

    contenedor.appendChild(pInfo);
    contenedor.appendChild(pTiempo);

    const intervalo = setInterval(() => {
        segundos--;
        if (segundos <= 0) {
            clearInterval(intervalo);
            contenedor.innerHTML = "";
            mostrarBotonAbrir(contenedor,tipoSobre);
        } else {
            pTiempo.textContent = formatearTiempo(segundos);
        }
    }, 1000);
    
    pTiempo.textContent = formatearTiempo(segundos);
}

function mostrarBotonAbrir(contenedor,tipoSobre) {
    const parrafo = document.createElement('p');
    parrafo.textContent = "Sobre ya disponible!";
    const boton = document.createElement('button');
    boton.textContent = "Abrir sobre";
    boton.classList.add('boton-abrir-sobre');
    boton.onclick = async () => {
        try{
            const equiposSobre = await abrirSobre(tipoSobre);
            imprimirSobreAbierto(equiposSobre);
        } catch(err){
            alert(err.mensaje);
        }
    };
    contenedor.appendChild(parrafo);
    contenedor.appendChild(boton);
}

function formatearTiempo(segundosTotales) {
    const horas = Math.floor(segundosTotales / 3600);
    const minutos = Math.floor((segundosTotales % 3600) / 60);
    const segundos = segundosTotales % 60;

    return `${horas.toString().padStart(2, '0')}:${minutos.toString().padStart(2, '0')}:${segundos.toString().padStart(2, '0')}`;
}

function imprimirSobreAbierto(equiposSobre){
    contenedor.innerHTML = "";
}

await imprimirSobres();