document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("comment-form");
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const messageInput = document.getElementById("commentMessage");
        const message = messageInput.value.trim();
        if (message.length === 0) return;
        console.log(messageInput.value);

        const actionUrl = form.getAttribute("action");
        const commentDto = {
            text: message
        };

        fetch(actionUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(commentDto)
        })
            .then(res => {
                if (!res.ok) throw new Error("Comment submission failed");
                return res.json(); // получаем CommentDto
            })
            .then(comment => {
                const noCommentsMsg = document.getElementById("no-comments-msg");
                if (noCommentsMsg) noCommentsMsg.style.display = "none";

                let commentsContainer = document.getElementById("comments");

                if (!commentsContainer) {
                    commentsContainer = document.createElement("div");
                    commentsContainer.id = "comments";
                    commentsContainer.className = "skill-comments";
                    form.insertAdjacentElement("afterend", commentsContainer);
                }

                const commentDiv = document.createElement("div");
                commentDiv.className = "skill-row";
                commentDiv.style.display = "block";

                const author = document.createElement("strong");
                author.textContent = comment.authorUsername;

                if (comment.parentId) {
                    const replyInfo = document.createElement("div");
                    replyInfo.className = "parent-comment";

                    const parentLabel = document.createElement("em");
                    parentLabel.textContent = `In reply to ${comment.parentAuthorUsername}:`;

                    const parentText = document.createElement("blockquote");
                    parentText.textContent = comment.parentText;

                    replyInfo.appendChild(parentLabel);
                    replyInfo.appendChild(parentText);
                    commentDiv.appendChild(replyInfo);
                }

                const text = document.createElement("p");
                text.textContent = comment.text;

                commentDiv.appendChild(author);
                commentDiv.appendChild(text);

                commentsContainer.insertBefore(commentDiv, commentsContainer.firstChild);

                const commentList = commentsContainer.querySelectorAll(".skill-row");
                const commentsCount = commentList.length;

                if (commentsCount > 10) {
                    commentList[commentsCount - 1].style.display = "none";
                }

                let pagination = commentsContainer.querySelector(".pagination");
                if (commentsCount > 10 && !pagination) {
                    pagination = document.createElement("div");
                    pagination.className = "pagination";

                    const nextLink = document.createElement("a");
                    nextLink.textContent = "Next »";

                    const baseUrl = form.dataset.baseurl;
                    const currentPage = 0;

                    nextLink.href = `${baseUrl}?page=${currentPage + 1}`;
                    pagination.appendChild(nextLink);
                    commentsContainer.appendChild(pagination);
                }

                messageInput.value = "";
            })
            .catch(err => {
                console.error(err);
                alert("Failed to submit comment.");
            });
    });
});
