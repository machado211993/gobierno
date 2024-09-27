// script.js
document.addEventListener("DOMContentLoaded", function() {
    // Desaparecer alertas después de 5 segundos
    const successAlert = document.querySelector('.mensajeExito');
    const errorAlert = document.querySelector('.mensajeError');
    
    if (successAlert) {
        setTimeout(() => {
            successAlert.style.display = 'none';
        }, 5000);
    }
    
    if (errorAlert) {
        setTimeout(() => {
            errorAlert.style.display = 'none';
        }, 5000);
    }
});

/*document.addEventListener("DOMContentLoaded", function() {
    const productCards = document.querySelectorAll('.card');

    productCards.forEach(card => {
        card.addEventListener('click', function() {
            const productName = this.querySelector('.card-text').innerText;
            const productPrice = this.querySelector('.card-title').innerText;
            
            alert(`Has seleccionado el producto: ${productName}, Precio: ${productPrice}`);
            
            // Aquí podrías agregar el producto a un carrito o hacer algo más dinámico.
        });
    });
});*/

// script.js
document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector('form');

    form.addEventListener('submit', function(event) {
        const nombreInput = document.querySelector('#nombre');
        const emailInput = document.querySelector('#email');
        
        if (nombreInput.value === '' || emailInput.value === '') {
            alert('Por favor, complete todos los campos.');
            event.preventDefault();  // Evita que el formulario se envíe si hay campos vacíos
        }
    });
});

/*document.addEventListener("DOMContentLoaded", function() {
    const carouselElement = document.querySelector('#carouselExample');

    carouselElement.addEventListener('mouseenter', function() {
        const carousel = new bootstrap.Carousel(carouselElement, {
            interval: false  // Pausa el carrusel
        });
    });

    carouselElement.addEventListener('mouseleave', function() {
        const carousel = new bootstrap.Carousel(carouselElement, {
            interval: 5000  // Reinicia el carrusel con el intervalo predeterminado
        });
    });
});*/

/*document.addEventListener("DOMContentLoaded", function() {
    const backToTopButton = document.getElementById('backToTop');
    
    window.addEventListener('scroll', function() {
        if (window.scrollY > 300) {
            backToTopButton.style.display = 'block';
        } else {
            backToTopButton.style.display = 'none';
        }
    });
    
    backToTopButton.addEventListener('click', function() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
});*/

document.addEventListener("DOMContentLoaded", function() {
    const deleteButtons = document.querySelectorAll('.btn-delete');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            const confirmed = confirm('¿Estás seguro de que deseas eliminar este elemento?');
            if (!confirmed) {
                event.preventDefault();  // Cancela la acción si el usuario elige "Cancelar"
            }
        });
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const toggleButton = document.getElementById('toggleDarkMode');
    const body = document.body;

    toggleButton.addEventListener('click', function() {
        body.classList.toggle('dark-mode');  // Activa/desactiva la clase 'dark-mode'

        // Guardar la preferencia en localStorage
        if (body.classList.contains('dark-mode')) {
            localStorage.setItem('darkMode', 'enabled');
        } else {
            localStorage.setItem('darkMode', 'disabled');
        }
    });

    // Mantener la preferencia en futuras visitas
    if (localStorage.getItem('darkMode') === 'enabled') {
        body.classList.add('dark-mode');
    }
});

// script.js
/*document.addEventListener("DOMContentLoaded", function() {
    const emailInput = document.getElementById('email');
    const errorMessage = document.getElementById('emailError');

    emailInput.addEventListener('input', function() {
        const emailPattern = /^[^@]+@[^@]+\.[a-zA-Z]{2,}$/;
        if (!emailPattern.test(emailInput.value)) {
            errorMessage.innerText = 'Por favor, ingrese un email válido.';
        } else {
            errorMessage.innerText = '';  // Limpia el mensaje de error si es válido
        }
    });
});*/
/*// script.js*/
/*document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector('form');
    const submitButton = document.querySelector('button[type="submit"]');

    form.addEventListener('input', function() {
        const allFieldsFilled = Array.from(form.elements).every(input => input.value.trim() !== '');
        submitButton.disabled = !allFieldsFilled;
    });
});*/
