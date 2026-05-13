import { obtenerEquipos } from "./api.js";
import { finalizarCarga, iniciarCarga } from "./loader.js";

const UrlObjeto = new URL(window.location.href);

let paginaActual = UrlObjeto.searchParams.get('pagina');
let cantidad = UrlObjeto.searchParams.get('cantidad');
let totalPaginas = 0;



const contenedorEquipos = document.querySelector('.contenedor-equipos');

await imprimirPaginaEquipos(paginaActual-1);

async function imprimirPaginaEquipos(pagina) {
    try {
        iniciarCarga(document.querySelector('main'));
        const response = await obtenerEquipos(pagina, cantidad);

        paginaActual = response.number;
        totalPaginas = response.totalPages;
        
        renderizarEquipos(response.content);
        actualizarControlesPaginacion();

        history.pushState(null, "", "equipos.html?pagina="+(paginaActual+1)+"&cantidad="+cantidad);
    } catch (error) {
        console.error(error.message);
    }
    finally{
        finalizarCarga(document.querySelector('main'));
    }
}

function renderizarEquipos(listaEquipos) {
    contenedorEquipos.innerHTML = '';

    listaEquipos.forEach(equipo => {
        const equipoHTML = document.createElement('div');
        equipoHTML.classList.add('contenedor-equipo');

        const imagenEquipo = document.createElement('img');
        imagenEquipo.src = equipo.imagen;
        equipoHTML.appendChild(imagenEquipo);

        const nombreEquipo = document.createElement('h3');
        const ligaPais = document.createElement('p');
        const pais = document.createElement('p');
        const tipoTier = document.createElement('p');
        
        nombreEquipo.textContent = equipo.nombre;
        ligaPais.textContent = equipo.liga.nombre + " ("+equipo.pais.nombre+")";
        tipoTier.textContent = "Tier: " + equipo.tier;
        


        equipoHTML.appendChild(nombreEquipo);
        equipoHTML.appendChild(ligaPais);

        equipoHTML.appendChild(tipoTier);
        contenedorEquipos.appendChild(equipoHTML);
    });
}


function actualizarControlesPaginacion() {
    const contenedor = document.querySelector('.contenedor-paginacion');
    contenedor.innerHTML = ''; // Limpiamos

    const rango = 2;
    let paginas = [];

    for (let i = 0; i < totalPaginas; i++) {
        if (
            i === 0 || // Primera página
            i === totalPaginas - 1 || // Última página
            (i >= paginaActual - rango && i <= paginaActual + rango) // Cercanas a la actual
        ) {
            paginas.push(i);
        } else if (
            (i === paginaActual - rango - 1) ||
            (i === paginaActual + rango + 1)
        ) {
            paginas.push('...');
        }
    }


    paginas = paginas.filter((item, index) => paginas.indexOf(item) === index);


    paginas.forEach(p => {
        if (p === '...') {
            const span = document.createElement('span');
            span.className = 'dots';
            span.innerText = '...';
            contenedor.appendChild(span);
        } else {
            const btn = document.createElement('button');
            btn.innerText = p + 1;
            if (p === paginaActual) {
                btn.className = 'active';
            }
            else {
                btn.onclick = () => imprimirPaginaEquipos(p);
            }
            contenedor.appendChild(btn);
        }
    });
}