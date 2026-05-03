import { getTokenCookie } from "./api.js";

const token = getTokenCookie();
if (!token) {
    window.location.href = "/iniciar_sesion.html";
}