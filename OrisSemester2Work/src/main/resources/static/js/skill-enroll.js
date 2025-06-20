document.addEventListener("DOMContentLoaded", function () {
    const enrollBtn = document.getElementById("enroll");
    const modal = document.getElementById("enroll-modal");
    const confirmBtn = document.getElementById("confirm-enroll");
    const rangeSelect = document.getElementById("schedule-range");
    const closeBtn = document.getElementById("enroll-close-btn");
    const slotsContainer = document.getElementById("slots-container");

    let selectedSlot = null;

    function formatSlot(slot) {
        const start = new Date(slot.start);
        const end = new Date(start.getTime() + slot.lengthMinutes * 60000);

        const options = { weekday: 'short', day: 'numeric', month: 'short' };
        const dateStr = start.toLocaleDateString(undefined, options);
        const timeStr = `${start.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - ${end.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;

        return `${dateStr} ${timeStr}`;
    }

    function renderSlots(slots) {
        slotsContainer.innerHTML = "";

        if (slots.length === 0) {
            slotsContainer.innerHTML = "<p>No available slots</p>";
            return;
        }

        slots.forEach(slot => {
            const slotDiv = document.createElement("div");
            slotDiv.className = "slot-item";
            slotDiv.textContent = formatSlot(slot);

            slotDiv.addEventListener("click", () => {
                document.querySelectorAll(".slot-item").forEach(el => el.classList.remove("selected"));
                slotDiv.classList.add("selected");
                selectedSlot = slot;
            });

            slotsContainer.appendChild(slotDiv);
        });
    }

    function loadSlots(rangeDays) {
        const username = enrollBtn.dataset.username;
        const skillId = enrollBtn.dataset.skillId;

        fetch(`/api/v1/user/${username}/skill/${skillId}/slots?range=${rangeDays}`)
            .then(res => res.json())
            .then(data => {
                renderSlots(data);
            })
            .catch(err => {
                console.error("Error fetching slots:", err);
                slotsContainer.innerHTML = "<p style='color: red;'>Failed to load slots</p>";
            });
    }

    enrollBtn.addEventListener("click", () => {
        modal.style.display = "flex";
        loadSlots(rangeSelect.value);
    });

    closeBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });

    rangeSelect.addEventListener("change", () => {
        loadSlots(rangeSelect.value);
    });

    confirmBtn.addEventListener("click", () => {
        const username = enrollBtn.dataset.username;
        const skillId = enrollBtn.dataset.skillId;
        if (!selectedSlot) {
            alert("Please choose slot");
            return;
        }
        fetch(`/api/v1/user/${username}/skill/${skillId}/sign-up`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(selectedSlot)
        })
            .then(res => {
                if (!res.ok) {
                    if (res.status === 403) {
                        return res.text().then(errorMessage => {
                            throw new Error(errorMessage);
                        });
                    } else {
                        throw new Error("Enroll failed");
                    }
                }
            })
            .then(data => {
                // Скрыть модалку
                modal.style.display = "none";

                // Сброс слотов и selectedSlot
                slotsContainer.innerHTML = "";
                selectedSlot = "7";

                // Показать сообщение
                const msg = document.getElementById("success-message");
                msg.classList.remove("hidden");
                msg.classList.add("visible");

                // Скрыть сообщение через 3 секунды
                setTimeout(() => {
                    msg.classList.remove("visible");
                    msg.classList.add("hidden");
                }, 3000);
            })
            .catch(err => {
                console.error("Enrollment failed:", err);
                const errorMsg = document.createElement('p')
                errorMsg.style.fontSize = '12px';
                errorMsg.style.color = 'red';
                errorMsg.innerHTML = err;
                document.getElementById("modal-content").appendChild(errorMsg);
            });
    });
});
