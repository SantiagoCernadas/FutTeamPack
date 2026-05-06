import { getTokenCookie,cerrarSesion } from "./api.js";

function generarNav(){
    const contenedor = document.querySelector('nav');
    contenedor.innerHTML = "";
    if(getTokenCookie() == null){
        contenedor.appendChild(generarA("Iniciar sesion","iniciar_sesion.html"));
    }
    else{
        contenedor.appendChild(generarA("Mi perfil",""));
    }
    contenedor.appendChild(generarA("Abrir sobres","sobres.html"))
    contenedor.appendChild(generarA("Equipos",""))

    if(getTokenCookie() != null){
        const cerrarSesionA = generarA("Cerrar sesión","iniciar_sesion.html");
        contenedor.appendChild(cerrarSesionA);
        cerrarSesionA.addEventListener('click', () => {
            cerrarSesion();
        })
    }
}

function generarA(texto,enlace){
    const a = document.createElement('a');
    a.textContent = texto;
    a.href = enlace;
    return a;
}

generarNav();