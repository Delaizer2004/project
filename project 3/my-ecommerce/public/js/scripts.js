let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Функція для додавання товару в кошик
function addToCart(productId, productName, productPrice) {
    const product = { id: productId, name: productName, price: productPrice };
    cart.push(product);
    localStorage.setItem('cart', JSON.stringify(cart));
    if (window.location.pathname === '/cart') {
        updateCart();
    }
}

// Функція для оновлення кошика
function updateCart() {
    const cartContainer = document.getElementById('cart-items');
    cartContainer.innerHTML = '';
    let total = 0;
    cart.forEach(product => {
        const productElement = document.createElement('div');
        productElement.innerText = `${product.name} - $${product.price}`;
        cartContainer.appendChild(productElement);
        total += product.price;
    });
    const totalElement = document.createElement('div');
    totalElement.innerHTML = `<strong>Total: $${total.toFixed(2)}</strong>`;
    cartContainer.appendChild(totalElement);
}

// Функція для оформлення замовлення
function checkout() {
    if (cart.length === 0) {
        alert('Your cart is empty');
        return;
    }

    fetch('/api/checkout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ cart })
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Checkout failed');
        }
    })
    .then(order => {
        alert(`Order successful! Order ID: ${order.id}`);
        cart = [];
        localStorage.removeItem('cart');
        updateCart();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('There was a problem with your checkout');
    });
}

// Завантаження продуктів на сторінці продуктів
if (window.location.pathname === '/products') {
    document.addEventListener('DOMContentLoaded', () => {
        fetch('/api/products')
            .then(response => response.json())
            .then(products => {
                const productsContainer = document.getElementById('products');
                products.forEach(product => {
                    const productElement = document.createElement('div');
                    productElement.classList.add('product');
                    productElement.innerHTML = `
                        <img src="https://via.placeholder.com/150" alt="${product.name}">
                        <h2>${product.name }</h2>
                        <p>${product.description}</p>
                        <p>Price: $${product.price}</p>
                        <button onclick="addToCart(${product.id}, '${product.name}', ${product.price})">Add to Cart</button>
                    `;
                    productsContainer.appendChild(productElement);
                });
            });
    });
}

// Додавання обробника подій для кнопки оформлення замовлення
if (window.location.pathname === '/cart') {
    document.addEventListener('DOMContentLoaded', updateCart);
    document.getElementById('checkout').addEventListener('click', checkout);
}
