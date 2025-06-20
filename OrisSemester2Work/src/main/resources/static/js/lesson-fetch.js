document.addEventListener("DOMContentLoaded", () => {
    // Настройки
    const pageSize = 7;                  // дней за одну загрузку
    let weekOffset = 0;                  // смещение в днях от сегодня

    // Создаём контейнер и кнопку
    const container = document.getElementById("lesson-container");

    const loadMoreBtn = document.createElement('button');
    loadMoreBtn.id = 'loadMoreBtn';
    loadMoreBtn.textContent = 'Load more';
    loadMoreBtn.style.display = 'block';
    loadMoreBtn.style.margin = '20px auto';
    loadMoreBtn.style.padding = '10px 20px';
    loadMoreBtn.style.cursor = 'pointer';
    loadMoreBtn.className = 'custom-button custom-button-2'
    document.body.appendChild(loadMoreBtn);



    const currentUsername = document.getElementById("authUser").textContent.trim();

    // Вспомогательная функция для форматирования даты
    const formatDate = date => date.toISOString().split('T')[0];

    // Загрузка уроков для недели с заданным смещением
    function loadWeek(offsetDays) {
        const today = new Date();
        const lower = new Date(today);
        lower.setDate(today.getDate() + offsetDays);
        const upper = new Date(today);
        upper.setDate(today.getDate() + offsetDays + pageSize);

        const lowerBound = formatDate(lower);
        const upperBound = formatDate(upper);

        return fetch(
            `/api/v1/me/lesson?lowerBoundDate=${lowerBound}&upperBoundDate=${upperBound}`
        )
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch lessons");
                return res.json();
            });
    }

    // Рендерим уроки за один отрезок из 7 дней, с учётом смещения
    function renderWeek(lessons, offsetDays) {
        const today = new Date();
        for (let i = 0; i < pageSize; i++) {
            const currentDate = new Date(today);
            currentDate.setDate(today.getDate() + offsetDays + i);

            const dayTitle = `${currentDate.getDate()} ${currentDate.toLocaleDateString('en-US',{ month: 'short' })} · ${currentDate.toLocaleDateString('en-US',{ weekday: 'long' })}`;
            const dayKey = currentDate.toDateString();

            const dayLessons = lessons.filter(lesson => {
                return new Date(lesson.startDateTime).toDateString() === dayKey;
            });

            const dayBlock = document.createElement('div');
            dayBlock.className = 'day-section';

            const dateHeader = document.createElement('h2');
            dateHeader.textContent = dayTitle;
            dateHeader.className = 'date-header';
            dayBlock.appendChild(dateHeader);

            if (dayLessons.length === 0) {
                const noTask = document.createElement('div');
                noTask.className = 'no-lesson';
                noTask.textContent = "No lessons";
                dayBlock.appendChild(noTask);
            } else {
                dayLessons.forEach((lesson, idx) => {
                    const lessonItem = document.createElement('div');
                    lessonItem.className = 'lesson-item';

                    const startTime = new Date(lesson.startDateTime)
                        .toLocaleTimeString('ru-RU',{ hour:'2-digit', minute:'2-digit' });
                    const endTime = new Date(lesson.endDateTime)
                        .toLocaleTimeString('ru-RU',{ hour:'2-digit', minute:'2-digit' });

                    const isTeacher = lesson.teacherUsername === currentUsername;
                    const role = isTeacher ? 'TEACHER' : 'STUDENT';
                    const otherUsername = isTeacher ? lesson.studentUsername : lesson.teacherUsername;

                    lessonItem.innerHTML = `
                        <div class="lesson-first-row">
                            <div class="lesson-title">${idx + 1}. ${lesson.skillName}</div>
                            <span class="lesson-meta ${role.toLowerCase()}">${role}</span>
                        </div>
                        <div class="lesson-meta">
                            <span class="lesson-time">${startTime} – ${endTime}</span> · 
                            <span class="lesson-username">@${otherUsername}</span>
                        </div>
                        <div class="lesson-status">${lesson.status}</div>
                    `;
                    lessonItem.addEventListener('click', () => {
                        const modal = document.getElementById('lesson-modal');
                        const info = document.getElementById('lesson-info');

                        const partnerLink = `/user/${otherUsername}`;
                        const skillLink = role === 'STUDENT'
                            ? `/user/${otherUsername}/skill/${lesson.skillId}`
                            : `/me/skill/${lesson.skillId}`;
                        const zoomUrl = role === 'STUDENT'
                            ? `${lesson.studentZoomUrl}`
                            : `${lesson.teacherZoomUrl}`

                        const canStart = role === 'TEACHER' && lesson.status === 'PLANNED'
                        const canEnd = role === 'TEACHER';
                        const canJoin = lesson.status === 'ACTIVE';

                        info.innerHTML = `
                            <div class="lesson-details">
                                <p><strong>Skill:</strong> <a href="${skillLink}" class="modal-link">${lesson.skillName}</a></p>
                                <p><strong>From:</strong> ${startTime}</p>
                                <p><strong>To:</strong> ${endTime}</p>
                                <p><strong>Status:</strong> <span id="lesson-status-text">${lesson.status}</span></p>
                                <p><strong>Role:</strong> ${role}</p>
                                <p><strong>Partner:</strong> <a href="${partnerLink}" class="modal-link">@${otherUsername}</a></p>
                            </div>
                            <div class="lesson-buttons" style="display: flex; justify-content: space-between; margin-top: 1rem;">
                                <button id="start-btn" ${lesson.status === 'PLANNED' ? '' : 'style="display: none"'} class="custom-button custom-button-1" ${canStart ? '' : 'disabled'}>Start</button>
                                <button id="end-btn" ${lesson.status === 'ACTIVE' ? '' : 'style="display: none"'}  class="custom-button custom-button-1">Complete</button>
                                <button id="button-join-link" class="custom-button zoom-button" ${canJoin ? '' : 'disabled'}>
                                    <a id="join-link" style="color: white" class="button-a" ${canJoin ? '' : 'disabled'} href="${zoomUrl || '#'}" target="_blank">Join the meeting</a>
                                </button>
                                
                            </div>
                        `;

                        modal.style.display = 'flex';

                        // Обработчик кнопки Start
                        const startBtn = document.getElementById('start-btn');
                        const endBtn = document.getElementById('end-btn');
                        const joinLink = document.getElementById('join-link');
                        const statusText = document.getElementById('lesson-status-text');

                        if (canStart) {
                            startBtn.addEventListener('click', () => {
                                const waitMsg = document.createElement('p');
                                waitMsg.innerHTML = `Please wait...`;
                                waitMsg.style.fontSize = '12px';
                                info.appendChild(waitMsg);
                                fetch(`/api/v1/me/lesson/${lesson.id}?status=ACTIVE`, {
                                    method: 'PATCH',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    }
                                })
                                    .then(res => {
                                        if (!res.ok) throw new Error("Failed to start lesson");
                                        waitMsg.style.display = "none";
                                        return res.json();
                                    })
                                    .then(updatedLesson => {
                                        const zoomUrl = role === 'STUDENT'
                                            ? `${updatedLesson.studentZoomUrl}`
                                            : `${updatedLesson.teacherZoomUrl}`
                                        // Обновление данных в модалке
                                        statusText.textContent = updatedLesson.status;
                                        joinLink.href = zoomUrl || '#';
                                        joinLink.disabled = false;
                                        document.getElementById("button-join-link").disabled = false;

                                        startBtn.remove();

                                        endBtn.style.display = "block";
                                    })
                                    .catch(err => {
                                        console.error(err);
                                        alert("Не удалось начать урок");
                                    });
                            });
                        }
                        if (canEnd) {
                            endBtn.addEventListener('click', () => {
                                const waitMsg = document.createElement('p');
                                waitMsg.innerHTML = `Completing the lesson...`;
                                waitMsg.style.fontSize = '12px';
                                info.appendChild(waitMsg);

                                fetch(`/api/v1/me/lesson/${lesson.id}?status=COMPLETE`, {
                                    method: 'PATCH',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    }
                                })
                                    .then(res => {
                                        if (!res.ok) throw new Error("Failed to complete lesson");
                                        waitMsg.remove();
                                        return res.json();
                                    })
                                    .then(updatedLesson => {
                                        statusText.textContent = updatedLesson.status;
                                        endBtn.disabled = true;

                                        joinLink.href = "#";
                                        joinLink.disabled = true;
                                        document.getElementById("button-join-link").disabled = true;
                                    })
                                    .catch(err => {
                                        console.error(err);
                                        alert("Не удалось завершить урок");
                                    });
                            });
                        }
                    });
                    dayBlock.appendChild(lessonItem);
                });
            }

            container.appendChild(dayBlock);
        }
    }

    // Инициалная загрузка первой недели
    loadWeek(weekOffset)
        .then(lessons => renderWeek(lessons, weekOffset))
        .catch(err => console.error(err));


    document.getElementById('lesson-close-btn').addEventListener('click', () => {
        document.getElementById('lesson-modal').style.display = 'none';
    });
    loadMoreBtn.addEventListener('click', () => {
        loadMoreBtn.disabled = true;
        loadWeek(weekOffset + pageSize)
            .then(lessons => {
                weekOffset += pageSize;
                renderWeek(lessons, weekOffset);
            })
            .catch(err => console.error(err))
            .finally(() => {
                loadMoreBtn.disabled = false;
            });
    });
});
