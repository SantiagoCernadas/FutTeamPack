import { getSobresDisponibles, abrirSobre } from "./api.js";
import { iniciarCarga, finalizarCarga } from "./loader.js";


const contenedor = document.querySelector('.contenedor-sobres');
const contenedorSobreAbierto = document.querySelector('.contenedor-sobre-abierto');
const listaSobre = document.querySelector('.lista-sobre');

async function sobresDisponibles() {
    const sobres = await getSobresDisponibles();
    return sobres;
}

async function imprimirSobres() {
    try {
        contenedor.innerHTML = "";
        iniciarCarga(contenedor);
        const listaSobres = await sobresDisponibles();


        listaSobres.forEach((sobre) => {

            const sobreHTML = document.createElement('div');
            sobreHTML.classList.add('contenedor-sobre');


            const tituloSobre = document.createElement('h3');
            const tipoSobre = sobre.tipoSobre.toLowerCase();
            tituloSobre.textContent = "Sobre " + tipoSobre;
            sobreHTML.appendChild(tituloSobre);


            const imagen = document.createElement('img');
            imagen.classList.add('imagen');
            imagen.src = sobre.imagen;
            sobreHTML.appendChild(imagen);

            const areaAccion = document.createElement('div');
            areaAccion.classList.add('area-accion');

            let tiempoRestante = sobre.segundosRestantes;

            if (tiempoRestante <= 0) {
                mostrarBotonAbrir(areaAccion, tipoSobre);
            } else {
                iniciarCronometro(areaAccion, tiempoRestante, tipoSobre);
            }

            sobreHTML.appendChild(areaAccion);
            contenedor.appendChild(sobreHTML);
        });
    }
    catch (err) {
        console.error(err.message);
    }
    finally {
        finalizarCarga(contenedor);
    }

}

function iniciarCronometro(contenedor, segundos, tipoSobre) {
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
            mostrarBotonAbrir(contenedor, tipoSobre);
        } else {
            pTiempo.textContent = formatearTiempo(segundos);
        }
    }, 1000);

    pTiempo.textContent = formatearTiempo(segundos);
}

function mostrarBotonAbrir(contenedorBoton, tipoSobre) {
    const parrafo = document.createElement('p');
    parrafo.textContent = "Sobre ya disponible!";
    const boton = document.createElement('button');
    boton.textContent = "Abrir sobre";
    boton.classList.add('boton-abrir-sobre');
    boton.onclick = async () => {
        try {
            contenedor.innerHTML = "";
            contenedorSobreAbierto.innerHTML = "";
            listaSobre.style.display = 'flex';
            iniciarCarga(listaSobre);
            const equiposSobre = await abrirSobre(tipoSobre);
            imprimirSobreAbierto(equiposSobre, tipoSobre);
        } catch (err) {
            alert(err.mensaje);
        }
        finally{
            finalizarCarga(listaSobre);
        }
    };
    contenedorBoton.appendChild(parrafo);
    contenedorBoton.appendChild(boton);
}

function formatearTiempo(segundosTotales) {
    const horas = Math.floor(segundosTotales / 3600);
    const minutos = Math.floor((segundosTotales % 3600) / 60);
    const segundos = segundosTotales % 60;

    return `${horas.toString().padStart(2, '0')}:${minutos.toString().padStart(2, '0')}:${segundos.toString().padStart(2, '0')}`;
}

function imprimirSobreAbierto(equiposSobre, tipoSobre) {


    document.getElementById('nombre-sobre').textContent = "Sobre " + tipoSobre;
    equiposSobre.forEach((equipo) => {
        const equipoHTML = document.createElement('div');
        equipoHTML.classList.add('contenedor-sobre-equipo');

        const imagenEquipo = document.createElement('img');
        imagenEquipo.classList.add('imagen-equipo');
        imagenEquipo.src = equipo.imagen;
        equipoHTML.appendChild(imagenEquipo);

        const nombreEquipo = document.createElement('p');
        nombreEquipo.classList.add('nombre_equipo');
        nombreEquipo.textContent = equipo.nombre;
        equipoHTML.appendChild(nombreEquipo);

        const tier = document.createElement('p');
        tier.textContent = "Tier: " + equipo.tier;
        equipoHTML.appendChild(tier);

        const divInfo = document.createElement('div');
        divInfo.classList.add('contenedor-equipo-info');
        const imagenLiga = document.createElement('img');
        imagenLiga.classList.add('imagen-liga');
        imagenLiga.src = equipo.liga.imagen;
        const imagenPais = document.createElement('img');
        imagenPais.classList.add('imagen-pais');
        imagenPais.src = equipo.pais.imagen;
        divInfo.appendChild(imagenLiga);
        divInfo.appendChild(imagenPais);

        equipoHTML.appendChild(divInfo);

        const cantidad = document.createElement('p');
        cantidad.textContent = "cantidad: " + equipo.cantidad;

        equipoHTML.appendChild(cantidad);

        if (equipo.cantidad == 1) {
            const etiquetaNuevo = document.createElement('p');
            etiquetaNuevo.classList.add('etiqueta-desbloqueado');
            etiquetaNuevo.textContent = "¡Nuevo!";
            equipoHTML.appendChild(etiquetaNuevo);
        }

        contenedorSobreAbierto.appendChild(equipoHTML);
    })
}

document.getElementById('boton-sobre-aceptar').addEventListener('click', () => {
    listaSobre.style.display = 'none';
    imprimirSobres();
});

await imprimirSobres();