document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".review-form");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = new FormData(form);
        const message = formData.get("message");
        const rate = formData.get("rate");

        const url = form.getAttribute("action");

        try {
            const res = await fetch(url, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message, rate })
            });

            if (!res.ok) {
                if (res.status === 403) {
                    throw new Error("You already left a rate or have not complete a lesson");
                }
                throw new Error("Failed to submit review");
            }

            const newRate = await res.json(); // ожидается RateDto

            let ratesContainer = document.getElementById("rates");
            const noRatesMsg = document.getElementById("no-rates-msg");

            // Если контейнер отзывов ещё не существует, создаём его
            if (!ratesContainer) {
                ratesContainer = document.createElement("div");
                ratesContainer.id = "rates";

                // Добавим базовый контейнер в DOM (в skill-description)
                const desc = document.querySelector(".skill-description");
                desc.appendChild(ratesContainer);

                // Удаляем или скрываем сообщение об отсутствии отзывов
                if (noRatesMsg) noRatesMsg.style.display = "none";
            }

            // Создаём HTML-элемент для нового отзыва
            const newReview = document.createElement("div");
            newReview.className = "skill-row";
            newReview.style.display = "block";
            newReview.innerHTML = `
                    <strong>${newRate.raterUsername}</strong>
                    <span> — ${newRate.rate}/5</span>
                    <p>${newRate.message}</p>
                `;

            // Вставляем в начало
            const firstReview = ratesContainer.querySelector(".skill-row");
            ratesContainer.insertBefore(newReview, firstReview);

            // Ограничиваем количество видимых отзывов (например, 5)
            const allReviews = ratesContainer.querySelectorAll(".skill-row");
            if (allReviews.length > 5) {
                allReviews[allReviews.length - 1].style.display = "none";
            }

            // Очистка формы
            form.reset();
        } catch (err) {
            console.error(err);
            const msg = document.getElementById("rate-error-message");
            msg.innerText = err;
            msg.classList.remove("hidden");
            msg.classList.add("visible");

            // Скрыть сообщение через 3 секунды
            setTimeout(() => {
                msg.classList.remove("visible");
                msg.classList.add("hidden");
            }, 3000);
        }
    });
});