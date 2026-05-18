import { getTokenCookie } from "./api.js";

const token = getTokenCookie();
if (!token) {
    location.replace("/iniciar_sesion.html");
}