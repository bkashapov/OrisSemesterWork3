document.addEventListener("DOMContentLoaded", function () {
    const skillForm = document.querySelector(".skill-form");
    const wrapper = document.querySelector(".skill-form-wrapper");
    const extraForm = document.getElementById("extra-form");
    const inputsContainer = document.getElementById("inputs-container");
    const addInputBtn = document.getElementById("add-input");
    const submitInputsBtn = document.getElementById("submit-inputs");


    // Добавление нового текстового инпута
    function addInput() {
        const inputGroup = document.createElement("div");
        inputGroup.classList.add("input-group");
        inputGroup.style.display = "flex";
        inputGroup.style.gap = "10px";
        inputGroup.style.marginBottom = "8px";

        const input = document.createElement("input");
        input.type = "text";
        input.name = "extra";
        input.placeholder = "Введите значение";
        input.required = true;
        input.style.flex = "1";

        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.textContent = "✕";
        removeBtn.style.padding = "4px 10px";
        removeBtn.style.cursor = "pointer";
        removeBtn.title = "Удалить";

        removeBtn.addEventListener("click", () => {
            const allGroups = inputsContainer.querySelectorAll(".input-group");
            if (allGroups.length > 1) {
                inputsContainer.removeChild(inputGroup);
            }
        });

        inputGroup.appendChild(input);
        inputGroup.appendChild(removeBtn);
        inputsContainer.appendChild(inputGroup);
    }


    addInputBtn.addEventListener("click", function () {
        const inputs = inputsContainer.querySelectorAll("input[type='text']");
        const lastInput = inputs[inputs.length - 1];

        // Не добавлять новый инпут, если последний пустой
        if (!lastInput || lastInput.value.trim() === "") {
            lastInput.focus();
            return;
        }

        addInput();
    });

    let createdSkillId = null;

    // Обработка первой формы (multipart)
    skillForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const formData = new FormData(skillForm);

        fetch("/api/v1/me/skill", {
            method: "POST",
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Ошибка при отправке формы");
                }
                return response.text();
            })
            .then(data => {
                createdSkillId = data;
                console.log("Успешно:", data);

                // Удаляем форму скилла
                wrapper.remove();

                // Показываем форму с текстовыми инпутами
                extraForm.classList.remove("hidden");

                // Сразу добавим один инпут
                addInput();
            })
            .catch(error => {
                console.error("Ошибка:", error);
                alert("Произошла ошибка при отправке формы.");
            });
    });

    // Обработка второй формы (список строк)
    extraForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const inputs = inputsContainer.querySelectorAll("input[type='text']");
        const values = Array.from(inputs).map(input => input.value.trim()).filter(v => v !== "");

        if (!createdSkillId) {
            alert("Ошибка: skillId не определён.");
            return;
        }
        if (values.length === 0) {
            window.location.href = `/me/skill/${createdSkillId}`;
            return;
        }
        console.log("Собранные строки:", values);

        // Отправка данных на сервер
        fetch(`/api/v1/me/skill/${createdSkillId}/schedule`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(values)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Ошибка при отправке доп. данных");
                }
                return response.text();
            })
            .then(() => {
                // Переход на страницу созданного скилла
                window.location.href = `/me/skill/${createdSkillId}`;
            })
            .catch(error => {
                console.error("Ошибка:", error);
                alert("Произошла ошибка при отправке данных.");
            });
    });
});
