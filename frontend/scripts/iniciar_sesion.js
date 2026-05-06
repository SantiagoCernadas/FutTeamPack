import { getTokenCookie, iniciarSesion } from "./api.js";

const mensajeError = document.getElementById('mensaje-error');

document.getElementById('boton_iniciar_sesion').addEventListener('click', async () =>{

    const nickname = document.getElementById('input_usuario').value;
    const contrasenia = document.getElementById('input_contrasenia').value;

    if(nickname == ""){
        setMensajeError("Ingresar Nickname");
        return;
    }
    if(contrasenia == ""){
        setMensajeError("Ingresar contraseña");
        return;
    }

    limpiarMensajeError();
    try{
        await iniciarSesion(nickname,contrasenia);
        location.replace("sobres.html");
    } catch (err){
        setMensajeError(err.mensaje);
    }
});


document.getElementById('input_usuario').addEventListener('input',limpiarMensajeError);
document.getElementById('input_contrasenia').addEventListener('input',limpiarMensajeError);

function limpiarMensajeError(event){
    mensajeError.textContent = "";
}

function setMensajeError(mensaje){
    mensajeError.textContent = mensaje;
}