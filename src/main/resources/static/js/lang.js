// Detect current language from URL (/vi/... or /en/...)
const lang = window.location.pathname.split("/")[1] || "vi";

// Define translations
const translations = {
    vi: {
        inStock: "Còn hàng",
        outStock: "Hết hàng",
        price: "Giá",
        size: "Kích thước",
        addToCart: "Thêm vào giỏ hàng",
        description: "Mô tả sản phẩm",
        relatedProducts: "Sản phẩm liên quan",
        sortDefault: "Mặc định",
        sortPriceAsc: "Giá tăng dần",
        sortPriceDesc: "Giá giảm dần",
        sortNameAsc: "Từ A - Z",
        sortNameDesc: "Từ Z - A",
        buy:"Đặt hàng",
        checkOut:"Thanh toán",
        cartStaus:"Giỏ hàng trống"
    },
    en: {
        inStock: "In stock",
        outStock: "Out of stock",
        price: "Price",
        size: "Size",
        addToCart: "Add to cart",
        description: "Product description",
        relatedProducts: "Related products",
        sortDefault: "Default",
        sortPriceAsc: "Price: Low to High",
        sortPriceDesc: "Price: High to Low",
        sortNameAsc: "A - Z",
        sortNameDesc: "Z - A",
        buy:"Buy",
        checkOut:"Check Out",
        cartStaus:"Your cart is empty",
    },
};

function t(key) {
    return translations[lang]?.[key] || key;
}
