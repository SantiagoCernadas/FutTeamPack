import { getTokenCookie } from "./api.js";

const token = getTokenCookie();
if (!token) {
    alert("Debes iniciar sesion.")
    window.location.href = "/iniciar_sesion.html";
}