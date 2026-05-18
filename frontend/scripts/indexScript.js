import {getTokenCookie} from "./api.js";

document.getElementById('boton_iniciar').addEventListener('click', () => {
    const token = getTokenCookie();
    if (token != null){
        window.location.href = "sobres.html";
    }
    else{
        window.location.href = "iniciar_sesion.html";
    }
})
