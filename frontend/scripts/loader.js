export function iniciarCarga(contenedor){
    const loader = document.createElement('div');
    loader.classList.add('overlay-loader');
    loader.innerHTML = '<div class="mini-spinner"</div>';
    contenedor.classList.add('contenedor-relativo');
    contenedor.appendChild(loader);
}

export function finalizarCarga(contenedor){
    const loader = contenedor.querySelector('.overlay-loader');
    if(loader){
        contenedor.removeChild(loader);
        contenedor.classList.remove('contenedor-relativo');
    }
}