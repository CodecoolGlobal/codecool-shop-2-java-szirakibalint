function setCartEvent() {
    let cartButton = document.getElementById("open-modal");
    cartButton.addEventListener("click", async () => {
        await buildModal();
    })
}

async function buildModal() {
    let modalContent = document.getElementById("modal-body");
    modalContent.innerHTML = "";
    const url = "/cart";
    let cartContent = await apiGet(url);

    let divContent = document.createElement("div");
    divContent.className = "cart-content";

    for (let product of cartContent) {
        let productDiv = document.createElement("div");
        productDiv.className = "product-cart";

        let productName = document.createElement("p");
        productName.innerText = product.name;

        let decreaseProduct = document.createElement("a");
        decreaseProduct.href = "#";
        decreaseProduct.dataset.id = product.id;
        decreaseProduct.innerText = "-"
        decreaseProduct.className = "decrease-cart";

        let productQuantity = document.createElement("p");
        productQuantity.innerText = product.quantity;
        productQuantity.className = "quantity";
        productQuantity.id = "quantity-" + product.id;

        let increaseProduct = document.createElement("a");
        increaseProduct.href = "#";
        increaseProduct.dataset.id = product.id;
        increaseProduct.className = "increase-cart";
        increaseProduct.innerText = "+";

        let productPrice = document.createElement("p");
        productPrice.innerText = product.price;

        productDiv.appendChild(productName);
        productDiv.appendChild(decreaseProduct);
        productDiv.appendChild(productQuantity);
        productDiv.appendChild(increaseProduct);
        productDiv.appendChild(productPrice);

        modalContent.appendChild(productDiv);
    }
    setProductIncreaserEvent();
    setProductDecreaserEvent();
}

function setProductIncreaserEvent() {
    let decreaserButtons = document.getElementsByClassName("increase-cart");
    for (let button of decreaserButtons) {
        button.addEventListener("click", async () => {
            const url = "/cart";
            const id = button.dataset.id;
            await apiPost(url, {"product_id" : id})
            await quantityChange(button.dataset.id);
        })
    }
}

function setProductDecreaserEvent() {
    let decreaserButtons = document.getElementsByClassName("decrease-cart");
    for (let button of decreaserButtons) {
        button.addEventListener("click", async () => {
            const url = "cart?product-id=" + button.dataset.id + "&type=single";
            await apiDelete(url);
            await quantityChange(button.dataset.id);
        })
    }
}

async function quantityChange(id) {
    const url = "/cart?product-id=" + id;
    let data = await apiGet(url);
    let count = data[0].quantity;
    document.getElementById("quantity-" + id).innerText = count;
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
async function apiDelete(url, payload="") {
    await fetch(url, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: 'DELETE',
        body: JSON.stringify(payload)
    })
}

function init() {
    setButtonEvents();
    setCheckoutButtonEvent();
    setCartEvent();
}

init();
