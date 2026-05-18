import { usuarioExiste,registrarse} from "./api.js";
import { finalizarCarga, iniciarCarga } from "./loader.js";

const mensajeError = document.getElementById('mensaje-error');
const mensajeVerificacion = document.getElementById('mensaje-existe-usuario')


const nickname = document.getElementById('input_usuario');
const contrasenia = document.getElementById('input_contrasenia');
const confirmaContrasenia = document.getElementById('input_confirma_contrasenia');

document.getElementById('boton-registrarse').addEventListener('click', async () => {
    try{
        if(nickname.value == ""){
            setMensajeError("Ingresar nombre de usuario.");
            return;
        }
        if(contrasenia.value == "" || confirmaContrasenia.value == ""){
            setMensajeError("Ingresar contraseña.");
            return;
        }
        if(contrasenia.value != confirmaContrasenia.value){
            setMensajeError("Las contraseñas no son iguales.");
            return;
        }

        iniciarCarga(document.querySelector('main'));
        await registrarse(nickname.value,contrasenia.value,confirmaContrasenia.value);
        document.querySelector('main').style.display = 'none';
        document.querySelector('.usuario-registrado').style.display = 'flex';
    }catch(err){
        setMensajeError(err.mensaje);
    }
    finally{
        finalizarCarga(document.querySelector('main'));
    }
})

document.getElementById('boton-verificar-usuario').addEventListener('click', async () => {
    try{
        if(nickname.value == ""){
            setMensajeVerificacion("Ingresar nombre de usuario.",'#ff5d65');
            return;
        }

        iniciarCarga(document.querySelector('main'));
        const response = await usuarioExiste(nickname.value);
        if(response.existe){
            setMensajeVerificacion("Usuario ya existente",'#ff5d65');
        }
        else{
            setMensajeVerificacion("Usuario disponible",'#80ef80');
        }
    }
    catch(err){
        setMensajeVerificacion("Error inesperado");
    }
    finally{
        finalizarCarga(document.querySelector('main'));
    }
})

document.getElementById('boton_iniciar_sesion').addEventListener('click', () => {
    location.replace("iniciar_sesion.html");
})


nickname.addEventListener('input',limpiarMensajeError);
nickname.addEventListener('input',limpiarMensajeVerificacion);
contrasenia.addEventListener('input',limpiarMensajeError);
confirmaContrasenia.addEventListener('input',limpiarMensajeError);

function limpiarMensajeError(event){
    mensajeError.textContent = "";
}

function limpiarMensajeVerificacion(event){
    mensajeVerificacion.textContent = "";
}

function setMensajeError(mensaje){
    mensajeError.textContent = mensaje;
}
function setMensajeVerificacion(mensaje,colorHex){
    mensajeVerificacion.textContent = mensaje;
    mensajeVerificacion.style.color = colorHex;
}