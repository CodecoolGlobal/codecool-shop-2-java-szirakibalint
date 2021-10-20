function getPayPalForm() {
    return `<label for="payment-form">Enter PayPal details:</label>
             <form id="payment-form">
              <div class="form-group">
                <label for="email">Email address</label>
                <input type="email" class="form-control" id="email" aria-describedby="emailHelp" placeholder="Enter email">
              </div>
              <div class="form-group">
                <label for="password">Password</label>
                <input type="password" class="form-control" id="password" placeholder="Password">
              </div>
              <button type="submit" class="btn btn-primary">Pay with PayPal</button>
            </form>`
}

function getCreditCardFrom() {
    return `<label for="payment-form">Enter Credit Card details:</label>
             <form id="payment-form">
              <div class="form-group">
                <label for="card-number">Card number:</label>
                <input type="text" class="form-control" id="card-number" placeholder="card number">
              </div>
              <div class="form-group">
                <label for="card-holder">Card holder:</label>
                <input type="text" class="form-control" id="card-holder" placeholder="card holder">
              </div>
              <div class="form-group">
                <label for="expiration">Expiration:</label>
                <input type="text" class="form-control" id="expiration" placeholder="expiration">
              </div>
              <div class="form-group">
                <label for="cvv">CVV:</label>
                <input type="text" minlength="3" maxlength="3" class="form-control" id="cvv" placeholder="CVV">
              </div>
              <button type="submit" class="btn btn-primary">Pay with Credit Card</button>
            </form>`
}

function initPaymentSelector() {
    const paymentSelector = document.querySelector("#payment-selector");
    paymentSelector.addEventListener("submit", e => {
        e.preventDefault();
        const paymentMethod = document.querySelector('input[name="payment-method"]:checked').value
        if (paymentMethod != null) {
            const paymentDiv = document.querySelector("#payment");
            if (paymentMethod === "paypal") {
                paymentDiv.innerHTML = getPayPalForm();
            } else {
                paymentDiv.innerHTML = getCreditCardFrom();
            }
        }
    })
}

initPaymentSelector();