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

function setButtonEvents() {
    let buttons = document.getElementsByClassName("btn-success");
    for (let button of buttons) {
        button.addEventListener("click", async (e) => {
            e.preventDefault();
            const card = button.parentElement.parentElement.parentElement;
            const id = card.id;
            const url = "/cart";
            await apiPost(url, {"product_id" : id});
            const cardName = card.getElementsByClassName("card-title")[0].innerHTML;
            alert(cardName + " Added to cart successfully!");
        })
    }
}

async function apiPost(url, payload) {
    await fetch(url, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'},
        method: 'POST',
        body: JSON.stringify(payload)
    })
}

async function apiGet(url) {
    let response = await fetch(url, {
        method: 'GET',
    })
    if (response.status === 200) {
        let data = response.json()
        return data
    }
}

function setCheckoutButtonEvent(){
    const button = document.getElementById("go-to-checkout")
    button.addEventListener('click', ()=>window.location='/checkout')
}

function init() {
    setButtonEvents();
    setCheckoutButtonEvent();
    setCartEvent();
}

init();
