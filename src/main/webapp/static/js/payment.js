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

function initPaymentSelector() {
    const paymentSelector = document.querySelector("#payment-selector");
    paymentSelector.addEventListener("submit", e => {
        e.preventDefault();
        const paymentMethod = document.querySelector('input[name="payment-method"]:checked').value
        if (paymentMethod != null) {
            const paymentDiv = document.querySelector("#payment");
            if (paymentMethod === "paypal") {
                paymentDiv.innerHTML = getPayPalForm();
            }
        }
    })
}

initPaymentSelector();