/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

function cargarInmueble(id) {
    alert("entro");
    fetch("/inmuebles/" + id)
            .then((data) => {
                document.getElementById("id").value = data.id;
                document.getElementById("titulo").value = data.titulo;
                document.getElementById("precioAlquiler").value = data.precioAlquiler;
                document.getElementById("precioVenta").value = data.precioVenta;
                document.getElementById("direccion").value = data.direccion;
                document.getElementById("expensas").value = data.expensas;
                document.getElementById("hectareas").value = data.hectareas;
                document.getElementById("mts2").value = data.mts2;
                document.getElementById("impMunicipales").value = data.impMunicipales;
                document.getElementById("impInmobiliarios").value = data.impInmobiliarios;
                document.getElementById("isVenta").value = data.isVenta;
                document.getElementById("isBanios").value = data.isBanios;
                document.getElementById("isAccesoRuta").value = data.isAccesoRuta;
                document.getElementById("isRiego").value = data.isRiego;
                document.getElementById("tipoInmueble").value = data.tipoInmueble;
                document.getElementById("propieatrio").value = data.propieatrio;
                // Carga los demás campos si es necesario
            })
            .catch((error) => console.error("Error al cargar el inmueble:", error));
}
