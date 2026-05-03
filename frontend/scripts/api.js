const BASE_URL = 'http://localhost:8080/';

async function callApi(endpoint, method = 'GET', body = null) {
    const token = localStorage.getItem('token');

    const settings = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}`})
        }
    };

    // Si hay cuerpo (para POST o PUT), lo convertimos a JSON
    if (body) {
        settings.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, settings);

        if (response.status === 401 || response.status === 403) {
        }

        if (response.status === 204) return null;

        const data = await response.json();

        if (!response.ok) {
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

function setTokenCookie(token) {
    document.cookie = "session_token="+token+"; path=/; max-age=86400;SameSite=Strict";
}

function cerrarSesion() {
    document.cookie = "session_token=; path=/; max-age=0;"
    window.location.href = "/iniciar_sesion.html";
}


export function getTokenCookie() {
    return document.cookie
    .split("; ")
    .find(row => row.startsWith("session_token="))
    ?.split("=")[1] ?? null;
}