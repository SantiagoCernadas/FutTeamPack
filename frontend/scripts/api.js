const BASE_URL = 'http://localhost:8080/';

const UrlObjeto = new URL(window.location.href);

async function callApi(endpoint, method = 'GET', body = null) {
    const token = getTokenCookie();

    const settings = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}`})
        }
    };

    if (body) {
        settings.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, settings);

        if (response.status === 204) return null;

        const data = await response.json();

        if (!response.ok) {
            if(data.error == 'No autorizado'){
                cerrarSesion();
            }
            throw data; 
        }

        return data;
    } catch (error) {
        throw error;
    }
}

export async function iniciarSesion(nickname,contrasenia) {
    const body = {nickname,contrasenia};
    try{
        const response = await callApi('auth/login', 'POST', body);
        setTokenCookie(response.token);
    }
    catch(err){
        throw err;
    }
}

export async function registrarse(nickname,contrasenia,confirmarContrasenia) {
    const body = {nickname,contrasenia,confirmarContrasenia};
    try{
        const response = await callApi('usuario', 'POST', body);
        return response;
    }
    catch(err){
        throw err;
    }
}

function setTokenCookie(token) {
    document.cookie = "session_token="+token+"; path=/; max-age=3600;SameSite=Strict";
}

export function cerrarSesion() {
    document.cookie = "session_token=; path=/; max-age=0;"
    window.location.href = "/iniciar_sesion.html";
}

export function getTokenCookie() {
    return document.cookie
    .split("; ")
    .find(row => row.startsWith("session_token="))
    ?.split("=")[1] ?? null;
}

export function getNickFromToken() {
  try {
    const base64Url = getTokenCookie().split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload).sub;
  } catch (e) {
    return null;
  }
}

export async function getSobresDisponibles() {
    try{
        const response = await callApi('packs', 'GET');
        return response;
    }
    catch(err){
        throw err;
    }
}

export async function abrirSobre(tipoSobre) {
    try{
        const response = await callApi('packs/' + tipoSobre, 'POST');
        return response;
    }
    catch(err){
        throw err;
    }
}

export async function obtenerUsuario(nickname) {
    try{
        const response = await callApi('usuario/' + nickname, 'GET');
        return response;
    }
    catch(err){
        throw err;
    }
}

export async function usuarioExiste(nickname) {
    try{
        const response = await callApi('usuario/'+nickname+'/existe', 'GET');
        return response;
    }
    catch(err){
        throw err;
    }
}

export async function obtenerEquipos(pagina = 0,cantidad = 5) {
    try{
        const response = await callApi('equipo/obtener'+'?pagina='+pagina+'&cantidad='+cantidad, 'GET');
        return response;
    }
    catch(err){
        throw err;
    }
}


export async function obtenerEquiposPorUsuario(nickname,pagina = 0,cantidad = 5) {
    try{
        const response = await callApi('equipo/usuario/' + nickname+"?pagina="+pagina+"&cantidad="+cantidad, 'GET');
        return response;
    }
    catch(err){
        throw err;
    }
}



