import {obtenerEquiposPorUsuario } from './api.js'

const UrlObjeto = new URL(window.location.href);
const nickname = UrlObjeto.searchParams.get('nickname');
let pagina = UrlObjeto.searchParams.get('pagina');
let cantidad  = UrlObjeto.searchParams.get('cantidad');

document.getElementById('nombre_usuario').textContent = nickname;

async function imprimirPaginaEquipos(){

}