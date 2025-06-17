document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('skillSearchInput');
    const skillsList = document.getElementById('skillsList');
    const username = window.location.pathname.split('/')[2];
    const noResultsMsg = document.getElementById('noResultsMsg') || createNoResultsMsg();
    const paginationContainer = document.getElementById('pagination') || createPaginationContainer();

    let debounceTimeout;
    let currentQuery = '';
    let currentPage = 0;
    const pageSize = 10;

    input.addEventListener('input', function () {
        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(() => {
            currentQuery = input.value.trim();
            currentPage = 0;
            fetchSkills(currentQuery, currentPage);
        }, 400);
    });

    function createNoResultsMsg() {
        const msg = document.createElement('p');
        msg.id = 'noResultsMsg';
        msg.textContent = 'Ничего не найдено';
        msg.style.display = 'none';
        skillsList.parentNode.insertBefore(msg, skillsList.nextSibling);
        return msg;
    }

    function createPaginationContainer() {
        const container = document.createElement('div');
        container.id = 'pagination';
        container.className = 'pagination';
        skillsList.parentNode.insertBefore(container, skillsList.nextSibling);
        return container;
    }

    async function fetchSkills(query, page) {
        try {
            const response = await fetch(
                `/api/v1/user/{username}/skill?query=${encodeURIComponent(query)}&pageNum=${page}&pageSize=${pageSize}`
            );
            const result = await response.json();
            const skills = result.content;

            skillsList.innerHTML = '';

            if (skills.length === 0) {
                noResultsMsg.style.display = 'block';
                paginationContainer.style.display = 'none';
                return;
            } else {
                noResultsMsg.style.display = 'none';
                paginationContainer.style.display = 'block';
            }

            skills.forEach(skill => {
                const link = document.createElement('a');
                link.href = `/user/${username}/skill/${skill.id}`;
                link.className = 'skill-row-link';

                link.innerHTML = `
                    <div class="skill-row">
                        <img src="${skill.imageFilename}" alt="Skill Image" class="skill-row-image"/>
                        <div class="skill-row-texts">
                            <div class="skill-row-name-rating">
                                <span class="skill-name">${skill.name}</span>
                                <span class="skill-rating">Rating: ${Number(skill.rating).toFixed(2)}</span>
                            </div>
                            <p class="skill-description">
                                ${skill.description.length > 50 ? skill.description.substring(0, 47) + '...' : skill.description}
                            </p>
                        </div>
                    </div>
                `;
                skillsList.appendChild(link);
            });

            renderPagination(result.totalPages, result.number);
        } catch (error) {
            console.error('Ошибка при загрузке навыков:', error);
        }
    }

    function renderPagination(totalPages, currentPageNum) {
        paginationContainer.innerHTML = '';

        for (let i = 0; i < totalPages; i++) {
            const btn = document.createElement('button');
            btn.textContent = i + 1;
            btn.disabled = i === currentPageNum;

            btn.addEventListener('click', () => {
                currentPage = i;
                fetchSkills(currentQuery, currentPage);
                window.scrollTo({ top: 0, behavior: 'smooth' }); // ← вот здесь
            });

            paginationContainer.appendChild(btn);
        }
    }

    // Инициализация при загрузке
    fetchSkills('', 0);
});
