import { iniciarSesion } from "./api.js";

document.getElementById('boton_iniciar_sesion').addEventListener('click', async () =>{

    const nickname = document.getElementById('input_usuario').value;
    const contrasenia = document.getElementById('input_contrasenia').value;

    try{
        await iniciarSesion(nickname,contrasenia);
        alert('sesion iniciada');
    } catch (err){
        alert(err.mensaje);
    }
});