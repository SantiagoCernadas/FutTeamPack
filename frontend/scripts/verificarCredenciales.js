import { getTokenCookie } from "./api";

const token = getTokenCookie();
if (!token) {
    window.location.href = "/iniciar_sesion.html";
}