document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("request-container");

    let currentPage = 0;
    const pageSize = 5;

    function fetchNotifications(pageNum) {
        container.querySelectorAll('.notification-list, .pagination-buttons, .empty-message, .error-message').forEach(el => el.remove());

        fetch(`/api/v1/me/notification?status=UNREAD&type=REQUEST&pageNum=${pageNum}&pageSize=${pageSize}`)
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch notifications");
                return res.json();
            })
            .then(data => {
                const notifications = data.content || [];
                const totalPages = data.totalPages || 1;

                if (notifications.length === 0) {
                    const empty = document.createElement('p');
                    empty.className = 'empty-message';
                    empty.textContent = "No unread requests.";
                    container.appendChild(empty);
                    return;
                }

                const list = document.createElement('div');
                list.className = 'notification-list';

                notifications.forEach(notification => {
                    const item = document.createElement('div');
                    item.className = 'notification-row';

                    const date = new Date(notification.startDateTime);
                    const formattedDate = date.toLocaleString('ru-RU', {
                        day: '2-digit', month: '2-digit', year: 'numeric',
                        hour: '2-digit', minute: '2-digit'
                    });

                    const content = document.createElement('div');
                    content.className = 'notification-text';
                    content.innerHTML = `
                        <a href="/user/${notification.fromUsername}" class="user-link">@${notification.fromUsername}</a> 
                        requested skill 
                        <a href="/me/skill/${notification.skillId}" class="skill-link">${notification.skillName}</a> 
                        at <span class="notification-time">${formattedDate}</span>
                    `;

                    const buttons = document.createElement('div');
                    buttons.className = 'notification-buttons';

                    const acceptBtn = document.createElement('button');
                    acceptBtn.textContent = 'Accept';
                    acceptBtn.className = 'accept-button';

                    const rejectBtn = document.createElement('button');
                    rejectBtn.textContent = 'Reject';
                    rejectBtn.className = 'reject-button';

                    acceptBtn.onclick = () => {
                        fetch(`/api/v1/me/request/${notification.id}/response?accept=true`, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ notificationId: notification.id })
                        })
                            .then(res => {
                                if (!res.ok) throw new Error();

                                acceptBtn.disabled = true;

                                // Делаем reject серой
                                rejectBtn.style.backgroundColor = '#ccc';
                                rejectBtn.style.color = '#666';
                                rejectBtn.disabled = true;
                            })
                            .catch(() => {
                                const msg = document.getElementById("error-message");
                                msg.classList.remove("hidden");
                                msg.classList.add("visible");

                                // Скрыть сообщение через 3 секунды
                                setTimeout(() => {
                                    msg.classList.remove("visible");
                                    msg.classList.add("hidden");
                                }, 3000);
                            });
                    };

                    rejectBtn.onclick = () => {
                        fetch(`/api/v1/me/request/${notification.id}/response?accept=false`, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ notificationId: notification.id })
                        })
                            .then(res => {
                                if (!res.ok) throw new Error();

                                rejectBtn.disabled = true;

                                // Делаем accept серой
                                acceptBtn.style.backgroundColor = '#ccc';
                                acceptBtn.style.color = '#666';
                                acceptBtn.disabled = true;
                            })
                            .catch(() => alert("Ошибка при отклонении запроса"));
                    };

                    buttons.appendChild(acceptBtn);
                    buttons.appendChild(rejectBtn);

                    item.appendChild(content);
                    item.appendChild(buttons);
                    list.appendChild(item);
                });

                container.appendChild(list);

                // Пагинация
                const pagination = document.createElement('div');
                pagination.className = 'pagination-buttons';

                const prevBtn = document.createElement('button');
                prevBtn.textContent = 'Previous';
                prevBtn.disabled = currentPage === 0;
                prevBtn.onclick = () => {
                    currentPage--;
                    fetchNotifications(currentPage);
                };

                const nextBtn = document.createElement('button');
                nextBtn.textContent = 'Next';
                nextBtn.disabled = currentPage >= totalPages - 1;
                nextBtn.onclick = () => {
                    currentPage++;
                    fetchNotifications(currentPage);
                };

                pagination.appendChild(prevBtn);
                pagination.appendChild(document.createTextNode(` Page ${currentPage + 1} of ${totalPages} `));
                pagination.appendChild(nextBtn);

                container.appendChild(pagination);
            })
            .catch(error => {
                console.error("Error:", error);
                const errMsg = document.createElement('p');
                errMsg.className = 'error-message';
                errMsg.textContent = "Ошибка загрузки уведомлений.";
                container.appendChild(errMsg);
            });
    }

    fetchNotifications(currentPage);
});
