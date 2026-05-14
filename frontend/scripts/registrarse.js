import { usuarioExiste,registrarse} from "./api.js";
import { finalizarCarga, iniciarCarga } from "./loader.js";

const mensajeError = document.getElementById('mensaje-error');
const mensajeVerificacion = document.getElementById('mensaje-existe-usuario')


const nickname = document.getElementById('input_usuario');
const contrasenia = document.getElementById('input_contrasenia');
const confirmaContrasenia = document.getElementById('input_confirma_contrasenia');

document.getElementById('boton-registrarse').addEventListener('click', async () => {

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
        setMensajeVerificacion("Error inessperado");
    }
    finally{
        finalizarCarga(document.querySelector('main'));
    }
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