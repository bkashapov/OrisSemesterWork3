document.addEventListener("DOMContentLoaded", () => {
    const userIcon = document.getElementById("user-icon");
    const sidebar = document.getElementById("sidebar");
    const closeBtn = document.getElementById("closeSidebar");

    if (userIcon && sidebar && closeBtn) {
        userIcon.addEventListener("click", () => {
            sidebar.classList.remove("hidden");
            sidebar.classList.add("visible");
        });

        closeBtn.addEventListener("click", () => {
            sidebar.classList.remove("visible");
            sidebar.classList.add("hidden");
        });

        // Закрытие при клике вне сайдбара
        document.addEventListener("click", (e) => {
            if (!sidebar.contains(e.target) && e.target !== userIcon) {
                sidebar.classList.remove("visible");
                sidebar.classList.add("hidden");
            }
        });
    }

    const logoutForm = document.getElementById("logout-form");

    if (logoutForm) {
        logoutForm.addEventListener("submit", (event) => {
            event.preventDefault(); // Остановить обычную отправку формы

            fetch("/logout", {
                method: "POST",
                credentials: "include"
            })
                .then(response => {
                    if (response.ok) {
                        location.reload(); // Обновляем страницу
                    } else {
                        alert("Ошибка при выходе из аккаунта");
                    }
                })
                .catch(() => {
                    alert("Сервер не отвечает");
                });
        });
    }
});