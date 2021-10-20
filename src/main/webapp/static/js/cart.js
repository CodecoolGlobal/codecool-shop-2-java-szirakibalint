function setCartEvent() {
    let cartButton = document.getElementById("open-modal");
    cartButton.addEventListener("click", async () => {
        let modalContent = document.getElementById("modal-body");
        const url = "/cart";
        let cartContent = await apiGet(url);

        //Needs to be implemented when we know what comes.

        modalContent.innerHTML = "My cart Content";
    })
}



function init() {
    setCartEvent();
}

init();
