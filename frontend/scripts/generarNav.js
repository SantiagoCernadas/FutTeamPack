import {cerrarSesion,getNickFromToken } from "./api.js";

function generarNav(){
    const contenedor = document.querySelector('nav');
    contenedor.innerHTML = "";
    const nickname = getNickFromToken(); 

    document.querySelector('h1').addEventListener('click', () => {
        window.location.href = "index.html";
    });

    if(nickname == null){
        contenedor.appendChild(generarA("Iniciar sesion","iniciar_sesion.html"));
    }
    else{
        contenedor.appendChild(generarA("Mi perfil","perfil.html?nickname=" + nickname));
    }
    contenedor.appendChild(generarA("Abrir sobres","sobres.html"))
    contenedor.appendChild(generarA("Equipos","equipos.html?pagina=1&cantidad=20"))

    if(nickname != null){
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