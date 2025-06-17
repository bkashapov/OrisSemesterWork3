document.addEventListener("DOMContentLoaded", () => {
    const loginBtn = document.getElementById("loginBtn");
    const modal = document.getElementById("loginModal");
    const closeBtn = modal.querySelector(".close-btn");
    const errorMsg = document.getElementById("loginError");
    const waitMsg = document.getElementById("wait");
    const username = document.getElementById("username");
    const password = document.getElementById("password");

    loginBtn.addEventListener("click", () => {
        modal.style.display = "flex"; // показать
    });

    closeBtn.addEventListener("click", () => {
        modal.style.display = "none";
        errorMsg.style.display = "none";
        waitMsg.style.display = "none";// скрыть
        username.value = "";
        password.value = "";
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("loginForm");
    const errorMsg = document.getElementById("loginError");
    const waitMsg = document.getElementById("wait");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        waitMsg.style.display = "block";
        const formData = new FormData(form);
        const params = new URLSearchParams(formData);

        const response = await fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: params,
        });

        if (response.ok) {
            location.reload();
        } else {
            waitMsg.style.display = "none";
            errorMsg.style.display = "block";
        }
    });
});
