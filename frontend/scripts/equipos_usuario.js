import { obtenerEquiposPorUsuario } from './api.js'

const UrlObjeto = new URL(window.location.href);
const nickname = UrlObjeto.searchParams.get('nickname');
let paginaActual = UrlObjeto.searchParams.get('pagina');
let cantidadPorPagina = UrlObjeto.searchParams.get('cantidad');
let totalPaginas = 0;


document.getElementById('nombre_usuario').textContent = nickname;

const contenedorEquipos = document.querySelector('.contenedor-equipos');

await imprimirPaginaEquipos(paginaActual);

async function imprimirPaginaEquipos(pagina) {
    try {
        const response = await obtenerEquiposPorUsuario(nickname, pagina, cantidadPorPagina);

        paginaActual = response.number;
        totalPaginas = response.totalPages;

        renderizarEquipos(response.content);

        actualizarControlesPaginacion();
    } catch (error) {
        console.error(error.message);
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
        const tipoTier = document.createElement('p');
        const cantidad = document.createElement('p');

        nombreEquipo.textContent = equipo.nombre;
        tipoTier.textContent = "Tier: " + equipo.tier;
        cantidad.textContent = "Cantidad: " + equipo.cantidad;

        equipoHTML.appendChild(nombreEquipo);
        equipoHTML.appendChild(tipoTier);
        equipoHTML.appendChild(cantidad);

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