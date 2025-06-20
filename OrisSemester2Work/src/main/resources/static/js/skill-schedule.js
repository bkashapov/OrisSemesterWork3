document.addEventListener("DOMContentLoaded", () => {
    const editBtn = document.getElementById("edit-schedule");
    const form = document.getElementById("extra-form");
    const skillContainer = document.getElementById("skill-container");
    const inputsContainer = document.getElementById("inputs-container");
    const addInputBtn = document.getElementById("add-input");

    // Функция добавления инпута с указанным значением
    function addInput(value = "") {
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
        input.value = value;

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

    // Добавление нового шаблона (если последний не пуст)
    addInputBtn.addEventListener("click", () => {
        const inputs = inputsContainer.querySelectorAll("input[type='text']");
        const lastInput = inputs[inputs.length - 1];
        if (!lastInput || lastInput.value.trim() === "") {
            lastInput.focus();
            return;
        }
        addInput();
    });

    const submitBtn = document.getElementById("submit-inputs");

    submitBtn.addEventListener("click", (e) => {
        e.preventDefault();

        const skillId = editBtn.getAttribute("data-skill-id");
        const inputs = inputsContainer.querySelectorAll("input[type='text']");
        const values = [];

        for (const input of inputs) {
            const val = input.value.trim();
            if (val !== "") {
                values.push(val);
            }
        }

        if (values.length === 0) {
            document.getElementById("scheduleErrorMsg").style.display = "block";
            return;
        }

        document.getElementById("scheduleErrorMsg").style.display = "none";

        fetch(`/api/v1/me/skill/${skillId}/schedule`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(values)
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to update schedule");
            })
            .then(() => {
                location.reload();
            })
            .catch(err => {
                console.error(err);
                alert("Не удалось сохранить расписание");
            });
    });


    // Обработка клика на кнопку Edit Schedule
    editBtn.addEventListener("click", () => {
        const skillId = editBtn.getAttribute("data-skill-id");

        fetch(`/api/v1/me/skill/${skillId}/schedule`)
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch schedule");
                return res.json();
            })
            .then(scheduleLines => {
                skillContainer.style.display = "none";
                form.classList.remove("hidden");

                inputsContainer.innerHTML = ""; // Очищаем все старые поля

                if (scheduleLines.length === 0) {
                    addInput(); // хотя бы один
                } else {
                    scheduleLines.forEach(line => addInput(line));
                }
            })
            .catch(err => {
                console.error(err);
                alert("Не удалось загрузить расписание");
            });
    });
});
