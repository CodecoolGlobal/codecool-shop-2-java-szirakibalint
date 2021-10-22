function setCartEvent() {
    let cartButton = document.getElementById("open-modal");
    console.log(cartButton);
    cartButton.addEventListener("click", async () => {
        await modal();
    })
}


async function modal() {
    let modalContent = document.getElementById("modal-body");
    modalContent.innerHTML = "";
    const url = "/cart?cart-id=" + sessionStorage.getItem("cart-id");
    let cartContent = await apiGet(url);
    let cartId = cartContent.id;
    sessionStorage.setItem("cart-id", cartId);

    let table = document.createElement("table");
    table.className = "table table-image";

    let tHead = document.createElement("thead");

    let picture = document.createElement("th");
    picture.scope = "col";

    let name = document.createElement("th")
    name.scope = "col";
    name.innerText = "Name";

    let price = document.createElement("th")
    price.scope = "col";
    price.innerText = "Price";

    let decrease = document.createElement("th");
    decrease.scope = "col";
    decrease.innerText = "Decrease";

    let quantity = document.createElement("th");
    quantity.scope = "col";
    quantity.innerText = "Quantity";

    let increase = document.createElement("th");
    increase.scope = "col";
    increase.innerText = "Increase";

    let deleteProduct = document.createElement("th");
    deleteProduct.scope = "col";
    deleteProduct.innerText = "Delete";

    tHead.appendChild(picture);
    tHead.appendChild(name);
    tHead.appendChild(price);
    tHead.appendChild(decrease);
    tHead.appendChild(quantity);
    tHead.appendChild(increase);
    tHead.appendChild(deleteProduct);

    table.appendChild(tHead);

    let tBody = document.createElement("tbody");

    for (let product of cartContent.products) {
        let tableRow = document.createElement("tr");

        let tData = document.createElement("td");
        tData.className = "w-25";

        let image = document.createElement("img");
        image.className = "img-fluid img-thumbnail";
        image.src = "/static/img/product_" + product.id + ".jpg";

        tData.appendChild(image);

        let productName = document.createElement("td");
        productName.innerText = product.name;

        let productPrice = document.createElement("td");
        productPrice.innerText = product.price + " USD";

        let productDecrease = document.createElement("td");
        let productDecreaseButton = document.createElement("a");
        productDecreaseButton.id = product.id;
        productDecreaseButton.dataset.id = product.id;
        productDecreaseButton.className = "decrease-cart";
        productDecreaseButton.href = "#";
        productDecreaseButton.innerText = "-";

        productDecrease.appendChild(productDecreaseButton);

        let productQuantity = document.createElement("td");
        productQuantity.id = "quantity-" + product.id;
        productQuantity.className = "product-quantity";
        productQuantity.innerText = product.quantity;

        let productIncrease = document.createElement("td");
        let productIncreaseButton = document.createElement("a");
        productIncreaseButton.id = product.id;
        productIncreaseButton.dataset.id = product.id;
        productIncreaseButton.className = "increase-cart";
        productIncreaseButton.href = "#";
        productIncreaseButton.innerText = "+";

        productIncrease.appendChild(productIncreaseButton);

        let productDelete = document.createElement("td");
        let productDeleteButton = document.createElement("a");
        productDeleteButton.href = "#";
        productDeleteButton.className = "btn btn-danger btn-sm delete-button";
        productDeleteButton.dataset.id = product.id;
        productDeleteButton.innerText = "Delete product";
        productDelete.appendChild(productDeleteButton);

        tableRow.appendChild(tData);
        tableRow.appendChild(productName);
        tableRow.appendChild(productPrice);
        tableRow.appendChild(productDecrease);
        tableRow.appendChild(productQuantity);
        tableRow.appendChild(productIncrease);
        tableRow.appendChild(productDelete);
        tBody.appendChild(tableRow);
    }
    table.appendChild(tBody);
    modalContent.appendChild(table);

    let total = document.createElement("div");
    total.className = "d-flex justify-content-end";
    let totalText = document.createElement("h5");
    totalText.innerText = "Total: "

    let totalPrice = document.createElement("span");
    totalPrice.className = "price text-success";
    totalPrice.innerText = cartContent.total_price + " USD";
    totalPrice.id = "total-price";

    totalText.appendChild(totalPrice);

    total.appendChild(totalText);
    modalContent.appendChild(total)

    setProductIncreaserEvent();
    setProductDecreaserEvent();
    setDeleteButtonEvents();
}

function setDeleteButtonEvents() {
    let buttons = document.getElementsByClassName("delete-button");
    for (let button of buttons) {
        button.addEventListener("click", async () => {
            const url = "cart?product-id=" + button.dataset.id + "&cart-id=" + sessionStorage.getItem("cart-id") +  "&type=multiple";
            await apiDelete(url);
            await modal();
        } )
    }
}

function setProductIncreaserEvent() {
    let decreaserButtons = document.getElementsByClassName("increase-cart");
    for (let button of decreaserButtons) {
        button.addEventListener("click", async () => {
            const url = "/cart";
            const id = button.dataset.id;
            await apiPost(url, {"product_id" : id, "cart_id": sessionStorage.getItem("cart-id")})
            await quantityChange(button.dataset.id);
        })
    }
}

function setProductDecreaserEvent() {
    let decreaserButtons = document.getElementsByClassName("decrease-cart");
    for (let button of decreaserButtons) {
        button.addEventListener("click", async () => {
            const url = "cart?product-id=" + button.dataset.id + "&cart-id=" + sessionStorage.getItem("cart-id") +  "&type=single";
            await apiDelete(url);
            await quantityChange(button.dataset.id);
        })
    }
}

async function quantityChange(id) {
    const url = "/cart?product-id=" + id + "&cart-id=" + sessionStorage.getItem("cart-id");
    let data = await apiGet(url);
    let count = data.quantity;
    let totalPrice = data.total_price;
    document.getElementById("quantity-" + id).innerText = count;
    document.getElementById("total-price").innerText = totalPrice + " USD";
    if (count == 0) {
        await modal();
    }
}

function setButtonEvents() {
    let buttons = document.getElementsByClassName("btn-success");
    for (let button of buttons) {
        button.addEventListener("click", async (e) => {
            e.preventDefault();
            const card = button.parentElement.parentElement.parentElement;
            const id = card.id;
            const url = "/cart";
            await apiPost(url, {"product_id" : id, "cart_id": sessionStorage.getItem("cart-id")});
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
    const buttons = document.getElementsByClassName("check-out");
    for (let button of buttons) {
        button.addEventListener('click', ()=>window.location='/checkout');
    }
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
    sessionStorage.setItem("cart-id", "0");
    setButtonEvents();
    setCheckoutButtonEvent();
    setCartEvent();
}

init();
