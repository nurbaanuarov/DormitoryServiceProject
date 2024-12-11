document.addEventListener('DOMContentLoaded', function () {
    const signupForm = document.querySelector('section');
    signupForm.style.opacity = 0;

    setTimeout(() => {
        signupForm.style.transition = 'opacity 1s ease-in-out';
        signupForm.style.opacity = 1;
    }, 500);

    const signupButton = document.querySelector('button');
    signupButton.addEventListener('click', function () {
        const email = document.querySelector('input[type="email"]');
        const password = document.querySelector('input[type="password"]');

        // Check for a valid email and password (you can add your validation logic here)
        const isValid = email.checkValidity() && password.checkValidity();

        if (!isValid) {
            signupForm.classList.add('shake');

            setTimeout(() => {
                signupForm.classList.remove('shake');
            }, 1000);
        }
    });
});

function navigateToSignup() {
    window.location.href = '/registration'; // Replace '/signup.html' with your actual signup page URL
}

function navigateToLogin() {
    window.location.href = '/login';
}